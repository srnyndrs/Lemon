package com.srnyndrs.android.lemon.ui.screen.main.content.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.domain.database.usecase.transaction.AllTransactionUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HomeViewModel.HomeViewModelFactory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted private val householdId: String,
    private val allTransactionUseCase: AllTransactionUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()
        .onStart { init() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeState()
        )


    @AssistedFactory
    interface HomeViewModelFactory {
        fun create(householdId: String): HomeViewModel
    }

    fun onEvent(event: HomeEvent) = viewModelScope.launch {
        _homeState.update {
            _homeState.value.copy(
                eventStatus = UiState.Loading()
            )
        }
        when(event) {
            is HomeEvent.DeleteTransaction -> {
                val transactionId = event.transactionId
                allTransactionUseCase.deleteTransactionUseCase(transactionId).fold(
                    onSuccess = {
                        fetchTransactions(householdId)
                    },
                    onFailure = { exception ->
                        _homeState.update {
                            _homeState.value.copy(
                                eventStatus = UiState.Error(exception.message ?: "Unknown error")
                            )
                        }
                    }
                )
            }
            is HomeEvent.SwitchHousehold -> {
                fetchTransactions(event.householdId)
            }
        }
    }

    private fun init() {
        // Avoid fetching when the householdId is not yet available (empty string).
        if (householdId.isBlank()) return
        fetchTransactions(householdId)
    }

    private fun fetchTransactions(householdId: String) = viewModelScope.launch {
        _homeState.update {
            _homeState.value.copy(transactions = UiState.Loading())
        }
        Log.d("SupabaseTransactionRepo", "Fetching transactions for householdId: $householdId")
        allTransactionUseCase.getMonthlyTransactionsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { transactions ->
                    _homeState.update {
                        _homeState.value.copy(
                            transactions = UiState.Success(transactions)
                        )
                    }

                    calculateExpenses(transactions)
                },
                onFailure = { exception ->
                    _homeState.update {
                        _homeState.value.copy(
                            transactions = UiState.Error(exception.message ?: "Unknown error")
                        )
                    }
                }
            )
        }
    }

    private fun calculateExpenses(transactions: Map<String, List<TransactionItem>>) {
        _homeState.update {
            _homeState.value.copy(expenses = UiState.Loading())
        }
        val expenses = mutableMapOf(
            TransactionType.EXPENSE to 0.0,
            TransactionType.INCOME to 0.0
        )
        transactions.values.flatten().forEach { transaction ->
            expenses[transaction.type] = expenses.getOrDefault(transaction.type, 0.0) + transaction.amount
        }
        _homeState.update {
            _homeState.value.copy(
                expenses = UiState.Success(expenses)
            )
        }
    }

    /*
    private fun calculateExpenses(transactions: Map<String, List<TransactionItem>>) {
        val expenses = mutableMapOf(
            TransactionType.EXPENSE to 0.0,
            TransactionType.INCOME to 0.0
        )
        transactions.values.flatten().forEach { transaction ->
            expenses[transaction.type] = expenses.getOrDefault(transaction.type, 0.0) + transaction.amount
        }
        _mainState.value = _mainState.value.copy(
            expenses = expenses
        )
    }
     */

}
