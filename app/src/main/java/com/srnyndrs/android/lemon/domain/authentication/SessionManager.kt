package com.srnyndrs.android.lemon.domain.authentication

import com.srnyndrs.android.lemon.domain.authentication.model.AuthStatus
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun listenSessionStatus(): Flow<AuthStatus>
    suspend fun logout(): Result<Unit>
}