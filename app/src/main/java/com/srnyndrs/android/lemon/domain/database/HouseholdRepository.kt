package com.srnyndrs.android.lemon.domain.database

interface HouseholdRepository {
    suspend fun createHousehold(name: String): Result<String>
}
