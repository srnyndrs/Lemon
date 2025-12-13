package com.srnyndrs.android.lemon.ui.screen.scan

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.domain.database.usecase.category.GetCategoriesUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.GetPaymentMethodsUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.transaction.AddTransactionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.PlatformImage
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

@HiltViewModel(assistedFactory = SplitBillViewModel.SplitBillViewModelFactory::class)
class SplitBillViewModel @AssistedInject constructor(
    @Assisted("household_id") private val householdId: String,
    @Assisted("user_id") private val userId: String,
    private val generativeModel: GenerativeModel,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
): ViewModel() {

    @AssistedFactory
    interface SplitBillViewModelFactory {
        fun create(
            @Assisted("household_id") householdId: String,
            @Assisted("user_id") userId: String
        ): SplitBillViewModel
    }

    private val _uiState = MutableStateFlow<SplitBillUiState>(SplitBillUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods = _paymentMethods.asStateFlow()

    init {
        fetchCategories()
        fetchPaymentMethods()
    }

    private fun fetchCategories() = viewModelScope.launch {
        getCategoriesUseCase(householdId).fold(
            onSuccess = { categories ->
                _categories.value = categories
                Log.d("SplitBillViewModel", "Fetched categories: $categories")
            },
            onFailure = { error ->
                Log.e("SplitBillViewModel", "Error fetching categories: ${error.message}", error)
            }
        )
    }

    private fun fetchPaymentMethods() = viewModelScope.launch {
        getPaymentMethodsUseCase(householdId).fold(
            onSuccess = { paymentMethods ->
                _paymentMethods.value = paymentMethods
            },
            onFailure = { error ->
                Log.e("SplitBillViewModel", "Error fetching payment methods: ${error.message}", error)
            }
        )
    }

    fun onEvent(event: ScanEvent) = viewModelScope.launch {
        when (event) {
            is ScanEvent.ExtractBillDataFromImage -> {
                extractBillDataFromImage(event.image)
            }
            is ScanEvent.ResetScanState -> {
                resetScanState()
            }
            is ScanEvent.SaveTransaction -> {
                _uiState.value = SplitBillUiState.Saving
                addTransactionUseCase(householdId, userId, event.transaction).fold(
                    onSuccess = {
                        Log.d("SplitBillViewModel", "Transaction saved successfully.")
                        _uiState.value = SplitBillUiState.Saved
                    },
                    onFailure = { error ->
                        Log.e("SplitBillViewModel", "Error saving transaction: ${error.message}", error)
                        _uiState.value = SplitBillUiState.Error("Failed to save transaction: ${error.message}")
                    }
                )
            }
        }
    }

    private fun imageProxyToByteArray(imageProxy: ImageProxy): ByteArray? {
        val bitmap = imageProxy.toBitmap()
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun extractBillDataFromImage(imageProxy: ImageProxy) = viewModelScope.launch {
        if (_uiState.value is SplitBillUiState.Loading) {
            imageProxy.close()
        } else {
            _uiState.value = SplitBillUiState.Loading
            try {
                val bitmap: ByteArray = imageProxyToByteArray(imageProxy) ?: throw Exception("Unable to process image")
                val prompt = createExtractionPrompt()
                Log.d("SplitBillViewModel", "Sending image to GenerativeModel")

                val response: GenerateContentResponse = generativeModel.generateContent(
                    content {
                        image(PlatformImage(bitmap))
                        text(prompt)
                    }
                )

                val responseText = response.text

                if (responseText != null) {
                    Log.d("SplitBillViewModel", "Received raw response: $responseText")
                    val cleanedJson = cleanJsonString(responseText)
                    Log.d("SplitBillViewModel", "Cleaned JSON for parsing: $cleanedJson")

                    if (cleanedJson.trimStart().startsWith("{")) {
                        // **FIX APPLIED HERE**
                        // Now, parseBillDetails will NOT crash on an empty or partial JSON.
                        val billDetails = parseBillDetails(cleanedJson)

                        // **IMPROVED LOGIC**
                        // After parsing, check if the result is meaningful. If the model
                        // returned an empty object, the details will be empty/default.
                        if (billDetails.title.isBlank() && billDetails.amount == 0.00) {
                            Log.w("SplitBillViewModel", "Parsing resulted in an empty BillDetails object. Assuming not a bill.")
                            _uiState.value = SplitBillUiState.Error("Could not detect a bill in the image. Please try again.")
                        } else {
                            // We have a valid, non-empty result.
                            _uiState.value = SplitBillUiState.Success(billDetails)
                        }
                    } else {
                        Log.e("SplitBillViewModel", "Model returned non-JSON text after cleaning: $cleanedJson")
                        _uiState.value = SplitBillUiState.Error("The image may not be a bill, or details could not be extracted.")
                    }
                } else {
                    Log.e("SplitBillViewModel", "Failed to extract details: Response text is null.")
                    val blockReason = response.promptFeedback?.blockReason?.toString()
                    var errorMessage = "Failed to get a response from the model."
                    if (blockReason != null) errorMessage += " Reason: $blockReason."
                    _uiState.value = SplitBillUiState.Error(errorMessage)
                }

            } catch (e: SerializationException) {
                Log.e("SplitBillViewModel", "JSON Parsing Error: ${e.message}", e)
                _uiState.value = SplitBillUiState.Error("Failed to understand the bill details format: ${e.message}")
            } catch (e: Exception) {
                Log.e("SplitBillViewModel", "Error extracting bill data: ${e.message}", e)
                _uiState.value = SplitBillUiState.Error("An error occurred: ${e.message}")
            } finally {
                try {
                    imageProxy.close()
                } catch (e: IllegalStateException) {
                    Log.w("SplitBillViewModel", "ImageProxy was already closed.")
                }
            }
        }
    }

    fun resetScanState() {
        _uiState.value = SplitBillUiState.Idle
    }

    private fun cleanJsonString(rawResponse: String): String {
        return rawResponse
            .trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

    private fun createExtractionPrompt(): String {
        // Provide machine-readable allowlists to reduce hallucination
        val allowedCategoriesJson = categories.value.joinToString(
                prefix = "[",
                postfix = "]",
                separator = ","
        ) { "{\"id\":\"${it.id}\",\"name\":\"${it.name}\"}" }

        val allowedPaymentMethodsJson = paymentMethods.value.joinToString(
            prefix = "[",
            postfix = "]",
            separator = ","
        ) { "{\"id\":\"${it.id}\",\"name\":\"${it.name}\"}" }

        return """
        You are extracting bill details from an image.

        Choose a category_id ONLY from the allowed list below. If unsure or no clear match, set category_id to null.
        AllowedCategories: $allowedCategoriesJson

        If payment method is present on the bill, choose a payment_method_id ONLY from the allowed list below. If unsure or no clear match, set payment_method_id to null.
        AllowedPaymentMethods: $allowedPaymentMethodsJson

        Matching rules:
        - Match by semantic meaning (merchant or line items) to the closest category name.
        - You MUST output a category_id that EXISTS in AllowedCategories.
        - If no category confidently matches, use null for category_id.
        - For payment methods, match recognizable indicators (e.g., card brand, wallet name like "OTP") to the closest allowed payment method name.
        - You MUST output a payment_method_id that EXISTS in AllowedPaymentMethods, otherwise use null.

        Output REQUIREMENTS:
        - Return a SINGLE JSON object with the following keys:
            {"title": string, "amount": number, "date": "YYYY-MM-DD", "category_id": string|null, "payment_method_id": string|null}
        - Numbers MUST be plain decimals (e.g., 45.67) without currency symbols.
        - Dates MUST be ISO 8601 format (YYYY-MM-DD). If unknown, use "".
        - If the image is not a bill, return {}.
        - Do NOT add any text before or after the JSON.

        Examples:
        - Valid with category & payment method:
            {"title":"The Food Place","amount":45.67,"date":"2024-07-30","category_id":"<one-of-allowed-ids>","payment_method_id":"<one-of-allowed-ids>"}
        - Valid without clear category or payment method:
            {"title":"The Food Place","amount":45.67,"date":"2024-07-30","category_id":null,"payment_method_id":null}
        - Not a bill:
            {}
        """
    }

    private val jsonParser = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun parseBillDetails(jsonString: String): TransactionDetailsDto {
        return jsonParser.decodeFromString<TransactionDetailsDto>(jsonString)
    }
}
