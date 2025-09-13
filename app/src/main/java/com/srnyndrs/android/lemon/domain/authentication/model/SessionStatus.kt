package com.srnyndrs.android.lemon.domain.authentication.model

sealed class SessionStatus {
    object Authenticated : SessionStatus()
    object Unauthenticated : SessionStatus()
    object Unknown : SessionStatus()
}
