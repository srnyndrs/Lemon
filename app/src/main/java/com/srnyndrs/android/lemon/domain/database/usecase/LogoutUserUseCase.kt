package com.srnyndrs.android.lemon.domain.database.usecase

import com.srnyndrs.android.lemon.domain.authentication.SessionManager
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        sessionManager.logout()
    }
}