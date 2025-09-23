package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Result<User>
}