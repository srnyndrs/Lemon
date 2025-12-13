package com.srnyndrs.android.lemon.ui.screen.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.genai.BillDetails
import com.srnyndrs.android.lemon.domain.genai.BillItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.PlatformImage
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class SplitBillViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
): ViewModel() {

    private val _uiState = MutableStateFlow<SplitBillUiState>(SplitBillUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun imageProxyToByteArray(imageProxy: ImageProxy): ByteArray? {
        val bitmap = imageProxy.toBitmap()
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun extractBillDataFromImage(imageProxy: ImageProxy) {
        if (_uiState.value is SplitBillUiState.Loading) {
            imageProxy.close()
            return
        }

        viewModelScope.launch {
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
                        if (billDetails.restaurantName.isBlank() && billDetails.items.isEmpty() && billDetails.total == "0.00") {
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

    fun updateBillDetails(billDetails: BillDetails) {
        _uiState.update {
            if (it is SplitBillUiState.Success) {
                it.copy(billDetails = billDetails)
            } else {
                it
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

    // Inside your SplitBillViewModel.kt
    private fun ImageProxy.toBitmap(): Bitmap {
        val planeProxy = planes[0] // <--- Problem might be here if 'planes' is not directly accessible or typed as expected
        val buffer = planeProxy.buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }



    private fun createExtractionPrompt(): String {
        return """
        Extract details from the bill image. Provide the output STRICTLY in a JSON format.
        Example:
        {
          "restaurantName": "The Food Place",
          "dateTime": "2024-07-30 12:34 PM",
          "items": [
            {"name": "Burger", "quantity": "1", "unitPrice": "15.00", "totalPrice": "15.00"}
          ],
          "subTotal": "25.00",
          "tax": "2.50",
          "service": "3.00",
          "discount": "0.00",
          "others": "0.00",
          "total": "30.50"
        }
        If info is not found, use "N/A" for strings or "0.00" for numbers.
        If the image is not a bill, return an empty JSON object: {}.
        DO NOT add any text or explanations outside the JSON object.
        """
    }

    private val jsonParser = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun parseBillDetails(jsonString: String): BillDetails {
        // This function is now safe from crashing on missing fields
        // because BillDetails has default values.
        return jsonParser.decodeFromString<BillDetails>(jsonString)
    }

    // --- Split Functionality (No changes needed here) ---

    private val _people = MutableStateFlow<List<String>>(emptyList())
    val people = _people.asStateFlow()

    private val _itemAssignments = MutableStateFlow<Map<BillItem, List<String>>>(emptyMap())
    val itemAssignments = _itemAssignments.asStateFlow()

    fun addPerson(name: String) {
        if (name.isNotBlank() && !_people.value.contains(name)) {
            _people.update { currentPeople -> currentPeople + name }
        }
    }

    fun removePerson(name: String) {
        _people.update { currentPeople -> currentPeople - name }
        _itemAssignments.update { currentAssignments ->
            val newAssignments = mutableMapOf<BillItem, List<String>>()
            currentAssignments.forEach { (item, assignedPeople) ->
                newAssignments[item] = assignedPeople.filterNot { it == name }
            }
            newAssignments
        }
    }

    fun assignItemToPerson(item: BillItem, personName: String) {
        _itemAssignments.update { currentMap ->
            val currentAssignmentsForItem = currentMap[item] ?: emptyList()
            val newAssignmentsForItem = if (currentAssignmentsForItem.contains(personName)) {
                currentAssignmentsForItem - personName
            } else {
                currentAssignmentsForItem + personName
            }
            currentMap + (item to newAssignmentsForItem)
        }
    }

    fun getSplitBillResult(): Map<String, Double> {
        val currentUiState = uiState.value
        if (currentUiState !is SplitBillUiState.Success) return emptyMap()

        val billDetails = currentUiState.billDetails
        val finalSplit = mutableMapOf<String, Double>()
        _people.value.forEach { person -> finalSplit[person] = 0.0 }

        billDetails.items.forEach { item ->
            val peopleSharingThisItem = _itemAssignments.value[item]?.filter { _people.value.contains(it) } ?: emptyList()
            val itemPrice = item.totalPrice.toDoubleOrNull() ?: 0.0

            if (peopleSharingThisItem.isNotEmpty() && itemPrice > 0) {
                val pricePerPersonForItem = itemPrice / peopleSharingThisItem.size
                peopleSharingThisItem.forEach { person ->
                    finalSplit[person] = (finalSplit[person] ?: 0.0) + pricePerPersonForItem
                }
            }
        }

        val subTotalOfAssignedItems = finalSplit.values.sum()
        val tax = billDetails.tax.toDoubleOrNull() ?: 0.0
        val service = billDetails.service.toDoubleOrNull() ?: 0.0
        val others = billDetails.others.toDoubleOrNull() ?: 0.0
        val discount = billDetails.discount.toDoubleOrNull() ?: 0.0
        val totalSharedCosts = tax + service + others - discount

        if (_people.value.isNotEmpty()) {
            if (subTotalOfAssignedItems > 0) {
                _people.value.forEach { person ->
                    val personSubTotal = finalSplit[person] ?: 0.0
                    val proportion = personSubTotal / subTotalOfAssignedItems
                    val personShareOfShared = totalSharedCosts * proportion
                    finalSplit[person] = personSubTotal + personShareOfShared
                }
            } else {
                val equalShare = totalSharedCosts / _people.value.size
                _people.value.forEach { person ->
                    finalSplit[person] = (finalSplit[person] ?: 0.0) + equalShare
                }
            }
        }

        finalSplit.forEach { (person, total) ->
            if (total < 0) finalSplit[person] = 0.0
        }

        return finalSplit
    }
}


sealed interface SplitBillUiState {
    data object Idle : SplitBillUiState
    data object Loading : SplitBillUiState
    data class Success(val billDetails: BillDetails) : SplitBillUiState
    data class Error(val message: String) : SplitBillUiState
}