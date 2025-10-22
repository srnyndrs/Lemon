package com.srnyndrs.android.lemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.authentication.SessionManager
import com.srnyndrs.android.lemon.domain.authentication.model.AuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager
): ViewModel() {
    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Unknown)
    val authStatus = _authStatus.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.listenSessionStatus().collect { sessionStatus ->
                _authStatus.value = sessionStatus
            }
        }
    }
}