package com.srnyndrs.android.lemon.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.User
import com.srnyndrs.android.lemon.domain.database.model.UserMainData
import com.srnyndrs.android.lemon.domain.database.usecase.GetUserUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.LogoutUserUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.category.AddCategoryUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.category.GetCategoriesUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.AddPaymentMethodUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.GetPaymentMethodsUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
import com.srnyndrs.android.lemon.ui.utils.UiState.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = MainViewModel.MainViewModelFactory::class)
class MainViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addPaymentMethodUseCase: AddPaymentMethodUseCase
): ViewModel() {

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(userId: String): MainViewModel
    }

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

    private val cache: MutableMap<String, MainState> = mutableMapOf()

    private val _user = MutableStateFlow<UiState<UserMainData>>(UiState.Empty())
    val user = _user.asStateFlow()
        .onStart {
            //fetchUser()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Empty()
        )

    fun onEvent(event: MainEvent<*>) = viewModelScope.launch {
        when(event) {
            is MainEvent.Logout -> {
                logoutUserUseCase()
            }
            is MainEvent.AddCategory -> {
                val currentHouseholdId = _mainState.value.selectedHouseholdId
                val category = event.data as Category
                addCategoryUseCase(category, currentHouseholdId).fold(
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
                addPaymentMethodUseCase(paymentMethod, currentHouseholdId, userId).fold(
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
                    cache[currentHouseholdId] = _mainState.value
                }
                // Get the household ID
                val householdId = event.data as String
                // Update selected household ID
                _mainState.value = _mainState.value.copy(
                    selectedHouseholdId = householdId
                )
                // Restore state from cache if exists
                val cachedState = cache[householdId]
                if (cachedState != null) {
                    _mainState.value = cachedState
                    return@launch
                }
                // Reload data
                fetchData(householdId)
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

    private suspend fun fetchData(householdId: String) {
        //_mainState.value = _mainState.value.copy(isLoading = true)
        getCategoriesUseCase(householdId).let { categories ->
            _mainState.value = _mainState.value.copy(
                categories = categories
            )
        }
        getPaymentMethodsUseCase(householdId).let { paymentMethods ->
            _mainState.value = _mainState.value.copy(
                paymentMethods = paymentMethods
            )
        }
    }

}