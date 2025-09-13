package com.srnyndrs.android.lemon.ui.screen.authentication

sealed class AuthenticationEvent {
    data class OnLoginClick(val email: String, val password: String) : AuthenticationEvent()
    data class OnRegisterClick(val email: String, val password: String) : AuthenticationEvent()
}