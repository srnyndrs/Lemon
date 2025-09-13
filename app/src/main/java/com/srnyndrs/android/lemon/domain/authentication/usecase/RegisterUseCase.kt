package com.srnyndrs.android.lemon.domain.authentication.usecase

import com.srnyndrs.android.lemon.domain.authentication.AuthenticationService
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authenticationService.registerWithEmail(email, password)
    }
}