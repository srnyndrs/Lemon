package com.srnyndrs.android.lemon.domain.authentication.usecase

import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> {
        return runCatching {
            authenticationService.login(username, password)
        }.getOrElse {
            Result.failure(it)
        }
    }
}