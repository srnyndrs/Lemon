package com.srnyndrs.android.lemon.domain.authentication

interface AuthenticationService {
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
    suspend fun registerWithEmail(email: String, password: String): Result<Unit>
}