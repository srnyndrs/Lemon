package com.srnyndrs.android.lemon.data.service

import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import kotlinx.coroutines.delay

class SupabaseAuthenticationService: AuthenticationService {
    override suspend fun login(username: String, password: String): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun register(username: String, password: String, email: String): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }
}