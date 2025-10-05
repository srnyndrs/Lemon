package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.UserMainData

interface UserRepository {
    suspend fun getUser(userId: String): Result<UserMainData>
}