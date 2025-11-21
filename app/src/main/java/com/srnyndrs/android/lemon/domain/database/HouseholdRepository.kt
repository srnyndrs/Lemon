package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.Household

interface HouseholdRepository {
    suspend fun createHousehold(name: String): Result<String>
    suspend fun getHousehold(householdId: String): Result<Household>
    suspend fun addMember(householdId: String, userId: String, role: String): Result<Unit>
    suspend fun removeMember(householdId: String, userId: String): Result<Unit>
    suspend fun updateMemberRole(householdId: String, userId: String, role: String): Result<Unit>
    suspend fun updateHouseholdName(householdId: String, name: String): Result<Unit>
    suspend fun deleteHousehold(householdId: String): Result<Unit>
}
