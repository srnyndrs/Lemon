package com.srnyndrs.android.lemon.ui.screen.main.content.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.usecase.transaction.AllTransactionUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
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

@HiltViewModel(assistedFactory = TransactionsViewModel.TransactionsViewModelFactory::class)
class TransactionsViewModel @AssistedInject constructor(
    @Assisted private val householdId: String,
    private val allTransactionUseCase: AllTransactionUseCase
): ViewModel() {

    @AssistedFactory
    interface TransactionsViewModelFactory {
        fun create(householdId: String): TransactionsViewModel
    }

    private val _transactionState = MutableStateFlow(TransactionState())
    val transactionState = _transactionState.asStateFlow()
        .onStart { fetchTransactions() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionState()
        )


    init {
        val (year, month) = getCurrentYearAndMonth()
        _transactionState.value = _transactionState.value.copy(
            selectedYear = year,
            selectedMonth = month
        )
    }

    private fun getCurrentYearAndMonth(): Pair<Int, Int> {
        val currentDate = java.time.LocalDate.now()
        return currentDate.year to currentDate.monthValue
    }

    fun onEvent(event: TransactionsEvent) = when(event) {
        is TransactionsEvent.ChangeDate -> {
            _transactionState.update {
                it.copy(
                    selectedYear = event.year,
                    selectedMonth = event.month
                )
            }
            fetchTransactions()
        }
    }

    private fun fetchTransactions() = viewModelScope.launch {
        _transactionState.value = _transactionState.value.copy(
            transactions = UiState.Loading()
        )
        val year = _transactionState.value.selectedYear
        val month = _transactionState.value.selectedMonth
        allTransactionUseCase.getMonthlyTransactionsUseCase(householdId, year, month).fold(
            onSuccess = { transactions ->
                _transactionState.value = _transactionState.value.copy(
                    transactions = UiState.Success(transactions)
                )
            },
            onFailure = { exception ->
                _transactionState.value = _transactionState.value.copy(
                    transactions = UiState.Error(exception.message ?: "Unknown error")
                )
            }
        )
    }

}