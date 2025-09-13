package com.srnyndrs.android.lemon.domain.authentication.model

import io.github.jan.supabase.auth.user.UserSession

sealed class SessionStatus {
    data class Authenticated(val userSession: UserSession): SessionStatus()
    object Unauthenticated : SessionStatus()
    object Unknown : SessionStatus()
}
