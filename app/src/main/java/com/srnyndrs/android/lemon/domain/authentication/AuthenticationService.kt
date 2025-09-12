package com.srnyndrs.android.lemon.domain.authentication

interface AuthenticationService {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun register(username: String, password: String, email: String): Result<Unit>
}