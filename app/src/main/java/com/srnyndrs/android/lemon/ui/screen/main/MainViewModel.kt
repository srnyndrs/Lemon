package com.srnyndrs.android.lemon.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.domain.database.usecase.user.GetUserUseCase
import com.srnyndrs.android.lemon.domain.authentication.usecase.LogoutUserUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.category.AllCategoryUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.household.AllHouseholdUseCase
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.AllPaymentMethodUseCase
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

@HiltViewModel(assistedFactory = MainViewModel.MainViewModelFactory::class)
class MainViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val allHouseholdUseCase: AllHouseholdUseCase
): ViewModel() {

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(userId: String): MainViewModel
    }

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()
        .onStart {
            fetchHouseholds()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainState()
        )

    fun onEvent(event: MainEvent) = viewModelScope.launch {
        when(event) {
            is MainEvent.Logout -> {
                logoutUserUseCase()
            }
            is MainEvent.SwitchHousehold -> {
                _mainState.value = _mainState.value.copy(
                    selectedHouseholdId = event.householdId
                )
            }
            is MainEvent.CreateHousehold -> {
                allHouseholdUseCase.createHouseholdUseCase(event.householdName).fold(
                    onSuccess = {
                        fetchHouseholds()
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
            is MainEvent.UpdateHouseholdName -> {
                updateHouseholdName(event.householdId, event.name)
            }
            is MainEvent.DeleteHousehold -> {
                deleteHousehold(event.householdId)
            }
        }
    }

    private fun fetchHouseholds() = viewModelScope.launch {
        _mainState.value = _mainState.value.copy(isLoading = true)
        getUserUseCase(userId).fold(
            onSuccess = { user ->
                val selectedHousehold = user.households[0].id
                _mainState.value = _mainState.value.copy(
                    user = user,
                    selectedHouseholdId = selectedHousehold,
                )
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

    private fun updateHouseholdName(householdId: String, name: String) {
        viewModelScope.launch {
            allHouseholdUseCase.updateHouseholdNameUseCase(householdId, name)
                .onSuccess {
                    fetchHouseholds()
                }
                .onFailure { error ->
                   // _uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    private fun deleteHousehold(householdId: String) {
        viewModelScope.launch {
            allHouseholdUseCase.deleteHouseholdUseCase(householdId)
                .onSuccess {
                    fetchHouseholds()
                }
                .onFailure { error ->
                    //_uiState.update { it.copy(household = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

}