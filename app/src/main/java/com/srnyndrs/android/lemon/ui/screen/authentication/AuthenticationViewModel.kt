package com.srnyndrs.android.lemon.ui.screen.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.authentication.usecase.LoginUseCase
import com.srnyndrs.android.lemon.domain.authentication.usecase.RegisterUseCase
import com.srnyndrs.android.lemon.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _authState = MutableStateFlow<UiState<Unit>>(UiState.Empty())
    val authState = _authState.asStateFlow()

    private fun processAuthentication(authMethod: suspend () -> Result<Unit>) = viewModelScope.launch {
        _authState.value = UiState.Loading()
        authMethod().fold(
            onSuccess = {
                _authState.value = UiState.Success(it)
            },
            onFailure = {
                _authState.value = UiState.Error("Authentication failed: ${it.message}")
            }
        )
    }

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.OnLoginClick -> {
                processAuthentication { loginUseCase(event.email, event.password) }
            }
            is AuthenticationEvent.OnRegisterClick -> {
                processAuthentication { registerUseCase(event.email, event.password) }
            }
        }
    }
}