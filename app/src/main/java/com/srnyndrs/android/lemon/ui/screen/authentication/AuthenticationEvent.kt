package com.srnyndrs.android.lemon.ui.screen.authentication

sealed class AuthenticationEvent {
    data class OnLoginClick(val username: String, val password: String) : AuthenticationEvent()
    data class OnRegisterClick(val username: String, val password: String, val email: String) : AuthenticationEvent()
}