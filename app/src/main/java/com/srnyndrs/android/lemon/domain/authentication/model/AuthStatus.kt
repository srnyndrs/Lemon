package com.srnyndrs.android.lemon.domain.authentication.model

import io.github.jan.supabase.auth.user.UserSession

sealed class AuthStatus {
    data class Authenticated(val userSession: UserSession): AuthStatus()
    object Unauthenticated : AuthStatus()
    object Unknown : AuthStatus()
}
