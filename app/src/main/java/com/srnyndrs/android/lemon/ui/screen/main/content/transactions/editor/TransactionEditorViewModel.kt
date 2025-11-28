package com.srnyndrs.android.lemon.ui.screen.main.content.transactions.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.domain.database.usecase.category.AllCategoryUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.AllPaymentMethodUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.transaction.AllTransactionUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TransactionEditorViewModel.TransactionEditorViewModelFactory::class)
class TransactionEditorViewModel @AssistedInject constructor(
    @Assisted("householdId") private val householdId: String,
    @Assisted("userId") private val userId: String,
    @Assisted("transactionId") private val transactionId: String?= null,
    private val allTransactionUseCase: AllTransactionUseCase,
    private val allCategoryUseCase: AllCategoryUseCase,
    private val allPaymentMethodUseCase: AllPaymentMethodUseCase
): ViewModel() {

    @AssistedFactory
    interface TransactionEditorViewModelFactory {
        fun create(
            @Assisted("householdId") householdId: String,
            @Assisted("userId") userId: String,
            @Assisted("transactionId") transactionId: String? = null
        ): TransactionEditorViewModel
    }

    private val _uiState = kotlinx.coroutines.flow.MutableStateFlow(TransactionEditorState())
    val uiState = _uiState.asStateFlow()
        .onStart {
            transactionId?.let {
                fetchTransaction(it)
            } ?: _uiState.update {
                it.copy(
                    transaction = UiState.Success(TransactionDetailsDto())
                )
            }
            fetchCategories()
            fetchPaymentMethods()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionEditorState()
        )

    fun onEvent(event: TransactionEditorEvent) = when(event) {
        is TransactionEditorEvent.AddTransaction -> {
            addTransaction(event.transaction)
        }
    }

    private fun addTransaction(
        transaction: TransactionDetailsDto
    ) = viewModelScope.launch {
        if(transactionId != null) {
            allTransactionUseCase.updateTransactionUseCase(
                householdId,
                userId,
                transactionId,
                transaction
            ).fold(
                onSuccess = {
                    // TODO
                },
                onFailure = {
                    // TODO
                }
            )
        } else {
            allTransactionUseCase.addTransactionUseCase(
                householdId,
                userId,
                transaction
            ).fold(
                onSuccess = {
                    // TODO
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    private fun fetchTransaction(transactionId: String) = viewModelScope.launch {
        allTransactionUseCase.getTransactionById(householdId, transactionId).fold(
            onSuccess = { transaction ->
                _uiState.update {
                    it.copy(
                        transaction = UiState.Success(transaction)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        transaction = UiState.Error(error.message ?: "Unknown error")
                    )
                }
            }
        )
    }

    private fun fetchCategories() = viewModelScope.launch {
        allCategoryUseCase.getCategoriesUseCase(householdId).fold(
            onSuccess = { categories ->
                _uiState.update {
                    it.copy(
                        categories = UiState.Success(categories)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        categories = UiState.Error(error.message ?: "Unknown error")
                    )
                }
            }
        )
    }

    private fun fetchPaymentMethods() = viewModelScope.launch {
        allPaymentMethodUseCase.getPaymentMethodsUseCase(householdId, userId).fold(
            onSuccess = { paymentMethods ->
                _uiState.update {
                    it.copy(
                        paymentMethods = UiState.Success(paymentMethods)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        paymentMethods = UiState.Error(error.message ?: "Unknown error")
                    )
                }
            }
        )
    }

}