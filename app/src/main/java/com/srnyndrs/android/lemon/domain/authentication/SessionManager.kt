package com.srnyndrs.android.lemon.domain.authentication

import com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun listenSessionStatus(): Flow<SessionStatus>
    suspend fun logout(): Result<Unit>
}