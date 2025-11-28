package com.srnyndrs.android.lemon.ui.screen.main.content.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.AllPaymentMethodUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.transaction.GetTransactionsByPaymentUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
import com.srnyndrs.android.lemon.ui.utils.UiState.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = WalletViewModel.WalletViewModelFactory::class)
class WalletViewModel @AssistedInject constructor(
    @Assisted("householdId") private val householdId: String,
    @Assisted("userId") private val userId: String,
    private val allPaymentMethodUseCase: AllPaymentMethodUseCase,
    private val getTransactionsUseCase: GetTransactionsByPaymentUseCase
): ViewModel() {

    @AssistedFactory
    interface WalletViewModelFactory {
        fun create(
            @Assisted("householdId") householdId: String,
            @Assisted("userId") userId: String
        ): WalletViewModel
    }

    private val _walletState = MutableStateFlow(WalletState())
    val walletState = _walletState.asStateFlow()
        .onStart {
            fetchPaymentMethods()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WalletState()
        )

    fun onEvent(event: WalletEvent) = viewModelScope.launch {
        when(event) {
            is WalletEvent.ChangePaymentMethod -> {
                fetchTransactions(event.paymentMethodId)
            }
            is WalletEvent.AddPaymentMethod -> {
                allPaymentMethodUseCase.addPaymentMethodUseCase(
                    event.paymentMethod,
                    householdId,
                    userId
                ).fold(
                    onSuccess = {
                        fetchPaymentMethods()
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
            is WalletEvent.UpdatePaymentMethod -> {
                allPaymentMethodUseCase.updatePaymentMethodUseCase(event.paymentMethod)
                    .fold(
                        onSuccess = {
                            fetchPaymentMethods()
                        },
                        onFailure = {
                            // TODO
                        }
                    )
            }
            is WalletEvent.AddPaymentMethodToHousehold -> {
                allPaymentMethodUseCase.linkPaymentMethodToHouseholdUseCase(
                    householdId,
                    event.paymentMethodId,
                ).fold(
                    onSuccess = {
                        fetchPaymentMethods()
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
            is WalletEvent.RemovePaymentMethodFromHousehold -> {
                allPaymentMethodUseCase.unlinkPaymentMethodFromHouseholdUseCase(
                    householdId,
                    event.paymentMethodId,
                ).fold(
                    onSuccess = {
                        fetchPaymentMethods()
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
            WalletEvent.ClearTransactions -> {
                _walletState.update {
                    it.copy(transactions = Success(emptyMap()))
                }
            }
            is WalletEvent.DeletePaymentMethod -> {
                allPaymentMethodUseCase.deletePaymentMethodUseCase(event.paymentMethodId)
                    .fold(
                        onSuccess = {
                            fetchPaymentMethods()
                        },
                        onFailure = {
                            // TODO
                        }
                    )
            }
        }
    }

    private fun fetchPaymentMethods() = viewModelScope.launch {
        _walletState.update { it.copy(paymentMethods = UiState.Loading()) }
        allPaymentMethodUseCase.getPaymentMethodsUseCase(householdId, userId).fold(
            onSuccess = { paymentMethods ->
                _walletState.update { it.copy(paymentMethods = UiState.Success(paymentMethods)) }
                paymentMethods.firstOrNull()?.id?.let {
                    fetchTransactions(it)
                }
            },
            onFailure = { exception ->
                _walletState.update { it.copy(paymentMethods = UiState.Error(exception.message ?: "Unknown error")) }
            }
        )
    }

    private fun fetchTransactions(paymentMethodId: String) = viewModelScope.launch {
        _walletState.update { it.copy(transactions = UiState.Loading()) }
        getTransactionsUseCase(householdId, paymentMethodId).fold(
            onSuccess = { transactions ->
                _walletState.update { it.copy(transactions = UiState.Success(transactions)) }
            },
            onFailure = { exception ->
                _walletState.update { it.copy(transactions = UiState.Error(exception.message ?: "Unknown error")) }
            }
        )
    }

}