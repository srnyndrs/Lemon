package com.srnyndrs.android.lemon.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.usecase.GetUserUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.LogoutUserUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.category.AllCategoryUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.AllPaymentMethodUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.transaction.AllTransactionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MainViewModel.MainViewModelFactory::class)
class MainViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val allCategoryUseCase: AllCategoryUseCase,
    private val allPaymentMethodUseCase: AllPaymentMethodUseCase,
    private val allTransactionUseCase: AllTransactionUseCase
): ViewModel() {

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(userId: String): MainViewModel
    }

    private val stateCache: MutableMap<String, MainState> = mutableMapOf()
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()
        .onStart {
            init()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainState()
        )

    fun onEvent(event: MainEvent<*>) = viewModelScope.launch {
        when(event) {
            is MainEvent.Logout -> {
                logoutUserUseCase()
            }
            is MainEvent.AddCategory -> {
                val currentHouseholdId = _mainState.value.selectedHouseholdId
                val category = event.data as Category
                allCategoryUseCase.addCategoryUseCase(category, currentHouseholdId).fold(
                    onSuccess = {
                        val currentCategories = _mainState.value.categories
                        _mainState.value = _mainState.value.copy(categories = currentCategories + it)
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
            is MainEvent.AddPaymentMethod -> {
                val currentHouseholdId = _mainState.value.selectedHouseholdId
                val paymentMethod = event.data as PaymentMethod
                allPaymentMethodUseCase.addPaymentMethodUseCase(paymentMethod, currentHouseholdId, userId).fold(
                    onSuccess = {
                        val currentPaymentMethods = _mainState.value.paymentMethods
                        _mainState.value = _mainState.value.copy(paymentMethods = currentPaymentMethods + it)
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
            is MainEvent.SwitchHousehold -> {
                // Save current state to cache
                val currentHouseholdId = _mainState.value.selectedHouseholdId
                if (currentHouseholdId.isNotBlank()) {
                    stateCache[currentHouseholdId] = _mainState.value
                }
                // Get the household ID
                val householdId = event.data as String
                // Update selected household ID
                _mainState.value = _mainState.value.copy(
                    selectedHouseholdId = householdId
                )
                // Restore state from cache if exists
                val cachedState = stateCache[householdId]
                if (cachedState != null) {
                    _mainState.value = cachedState
                    return@launch
                }
                // Reload data
                fetchData(householdId)
            }
            is MainEvent.AddTransaction -> {
                val transactionDetailsDto = event.data
                val currentHouseholdId = _mainState.value.selectedHouseholdId
                if (transactionDetailsDto != null) {
                    allTransactionUseCase.addTransactionUseCase(
                        householdId = currentHouseholdId,
                        userId = userId,
                        transactionDetailsDto = transactionDetailsDto
                    ).fold(
                        onSuccess = {
                            fetchTransactions(currentHouseholdId)
                            fetchStatistics(currentHouseholdId)
                        },
                        onFailure = {
                            // TODO
                        }
                    )
                }
            }
        }
    }

    private fun init() = viewModelScope.launch {
        _mainState.value = _mainState.value.copy(isLoading = true)
        getUserUseCase(userId).fold(
            onSuccess = { user ->
                val selectedHousehold = user.households[0].id
                _mainState.value = _mainState.value.copy(
                    user = user,
                    selectedHouseholdId = selectedHousehold,
                )
                fetchData(householdId = selectedHousehold)
                //
                _mainState.value = _mainState.value.copy(isLoading = false)
            },
            onFailure = { exception ->
                _mainState.value = _mainState.value.copy(
                    isLoading = false,
                    error = "An error occurred: ${exception.message}"
                )
            }
        )
    }

    private suspend fun fetchTransactions(householdId: String) {
        allTransactionUseCase.getMonthlyTransactionsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { transactions ->
                    _mainState.value = _mainState.value.copy(
                        transactions = transactions
                    )
                },
                onFailure = { exception ->
                    _mainState.value = _mainState.value.copy(
                        error = "An error occurred: ${exception.message}"
                    )
                }
            )
        }
    }

    private suspend fun fetchStatistics(householdId: String) {
        allTransactionUseCase.getMonthlyStatsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { statistics ->
                    _mainState.value = _mainState.value.copy(
                        statistics = statistics
                    )
                },
                onFailure = { exception ->
                    _mainState.value = _mainState.value.copy(
                        error = "An error occurred: ${exception.message}"
                    )
                }
            )
        }
    }

    private suspend fun fetchData(householdId: String) {
        allCategoryUseCase.getCategoriesUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { categories ->
                    _mainState.value = _mainState.value.copy(
                        categories = categories
                    )
                },
                onFailure = { exception ->
                    _mainState.value = _mainState.value.copy(
                        error = "An error occurred: ${exception.message}"
                    )
                }
            )
        }
        allPaymentMethodUseCase.getPaymentMethodsUseCase(householdId).let { response ->
            response.fold(
                onSuccess = { paymentMethods ->
                    _mainState.value = _mainState.value.copy(
                        paymentMethods = paymentMethods
                    )
                },
                onFailure = { exception ->
                    _mainState.value = _mainState.value.copy(
                        error = "An error occurred: ${exception.message}"
                    )
                }
            )
        }
        fetchTransactions(householdId)
        fetchStatistics(householdId)
    }

}