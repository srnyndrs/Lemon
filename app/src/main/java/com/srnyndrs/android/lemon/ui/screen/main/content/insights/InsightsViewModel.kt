package com.srnyndrs.android.lemon.ui.screen.main.content.insights

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

@HiltViewModel(assistedFactory = InsightsViewModel.InsightsViewModelFactory::class)
class InsightsViewModel @AssistedInject constructor(
    @Assisted private val householdId: String,
    private val allTransactionUseCase: AllTransactionUseCase
): ViewModel() {

    @AssistedFactory
    interface InsightsViewModelFactory {
        fun create(household: String): InsightsViewModel
    }

    private val _insightsState = MutableStateFlow(InsightsState())
    val insightsState = _insightsState.asStateFlow()
        .onStart {
            init()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InsightsState()
        )

    private fun init() = viewModelScope.launch {
        fetchStatistics(householdId)
        fetchAllExpenses(householdId)
        fetchAllIncomes(householdId)
    }

    private suspend fun fetchStatistics(householdId: String) {
        allTransactionUseCase.getMonthlyStatsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { statistics ->
                    _insightsState.update {
                        it.copy(statistics = UiState.Success(statistics))
                    }
                },
                onFailure = { exception ->
                    _insightsState.update {
                        it.copy(statistics = UiState.Error(exception.message ?: "Unknown error"))
                    }
                }
            )
        }
    }

    private suspend fun fetchAllExpenses(householdId: String) {
        allTransactionUseCase.getStatisticsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { allExpenses ->
                    _insightsState.update {
                        it.copy(allExpenses = UiState.Success(allExpenses))
                    }
                },
                onFailure = { exception ->
                    _insightsState.update {
                        it.copy(allExpenses = UiState.Error(exception.message ?: "Unknown error"))
                    }
                }
            )
        }
    }

    private suspend fun fetchAllIncomes(householdId: String) {
        allTransactionUseCase.getIncomeStatisticsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { allIncomes ->
                    _insightsState.update {
                        it.copy(allIncomes = UiState.Success(allIncomes))
                    }
                },
                onFailure = { exception ->
                    _insightsState.update {
                        it.copy(allIncomes = UiState.Error(exception.message ?: "Unknown error"))
                    }
                }
            )
        }
    }

}