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
import com.srnyndrs.android.lemon.domain.database.usecase.payment_method.GetPaymentMethodsUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
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
    private val addCategoryUseCase: AddCategoryUseCase
): ViewModel() {

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(userId: String): MainViewModel
    }

    private val _user = MutableStateFlow<UiState<UserMainData>>(UiState.Empty())
    val user = _user.asStateFlow()
        .onStart {
            fetchUser()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Empty()
        )


    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Empty())
    val categories = _categories.asStateFlow()

    private val _paymentMethods = MutableStateFlow<UiState<List<PaymentMethod>>>(UiState.Empty())
    val paymentMethods = _paymentMethods.asStateFlow()

    fun onEvent(event: MainEvent<*>) = viewModelScope.launch {
        when(event) {
            is MainEvent.Logout -> {
                logoutUserUseCase()
            }
            is MainEvent.FetchCategories -> {
                val householdId = event.householdId
                fetchCategories(householdId)
            }
            is MainEvent.AddCategory -> {
                // TODO
                val householdId = (_user.value as? UiState.Success)?.data?.households?.get(0)?.id ?: return@launch
                val category = event.data as Category
                addCategoryUseCase(category, householdId).fold(
                    onSuccess = {
                        val currentCategories = (_categories.value as? UiState.Success)?.data ?: emptyList()
                        _categories.value = UiState.Success(currentCategories + it)
                    },
                    onFailure = {
                        // TODO
                    }
                )
            }
        }
    }

    private fun fetchUser() = viewModelScope.launch {
        _user.value = UiState.Loading()
        getUserUseCase(userId).fold(
            onSuccess = {
                _user.value = UiState.Success(it)
                // TODO
                val selectedHousehold = it.households[0].id
                fetchCategories(selectedHousehold)
                fetchPaymentMethods(selectedHousehold)
            },
            onFailure = {
                _user.value = UiState.Error("Failed to fetch user: ${it.message}")
            }
        )
    }

    private fun fetchCategories(householdId: String) = viewModelScope.launch {
        _categories.value = UiState.Loading()
        getCategoriesUseCase(householdId).let {
            // TODO
            _categories.value = UiState.Success(it)
        }
    }

    private fun fetchPaymentMethods(householdId: String) = viewModelScope.launch {
        _paymentMethods.value = UiState.Loading()
        getPaymentMethodsUseCase(householdId).let {
            // TODO
            _paymentMethods.value = UiState.Success(it)
        }
    }

}