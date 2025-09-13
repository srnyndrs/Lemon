package com.srnyndrs.android.lemon.ui

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srnyndrs.android.lemon.domain.authentication.SessionManager
import com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager
): ViewModel() {

    private val _sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.Unknown)
    val sessionStatus = _sessionStatus.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.listenSessionStatus().collect { sessionStatus ->
                _sessionStatus.value = sessionStatus
            }
        }
    }

    fun logout() = viewModelScope.launch {
        sessionManager.logout()
    }

}