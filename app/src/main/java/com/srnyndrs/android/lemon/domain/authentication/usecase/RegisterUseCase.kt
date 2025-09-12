package com.srnyndrs.android.lemon.domain.authentication.usecase

import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(username: String, password: String, email: String): Result<Unit> {
        return runCatching {
            authenticationService.register(username, password, email)
        }.getOrElse {
            Result.failure(it)
        }
    }
}