package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.UserDto
import com.srnyndrs.android.lemon.data.database.dto.UserWithHousehold
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.domain.database.UserRepository
import com.srnyndrs.android.lemon.domain.database.model.UserMainData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class SupabaseUserRepository @Inject constructor(
    private val client: SupabaseClient
): UserRepository {

    private val _tag = "SupabaseUserRepo"

    override suspend fun getUser(userId: String): Result<UserMainData> {
        Log.d(_tag, "getUser() called with: userId = $userId")
        try {
            val response = client
                .from(DatabaseEndpoint.USER_HOUSEHOLDS_VIEW.path)
                .select {
                    filter { UserWithHousehold::userId eq userId }
                }
                .decodeList<UserWithHousehold>()
            val user = response.toDomain()
            Log.d(_tag, "getUser() returned: $user")
            return Result.success(user)
        } catch (e: Exception) {
            Log.e(_tag, "getUser() failed", e)
            return Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<Pair<String, String>>> {
        Log.d(_tag, "getUsers() called")
        return try {
            val response = client
                .from(DatabaseEndpoint.USERS_VIEW.path)
                .select()
                .decodeList<UserDto>()
            val users = response.map { it.id to it.username }
            Log.d(_tag, "getUsers() returned: $users")
            Result.success(users)
        } catch (e: Exception) {
            Log.e(_tag, "getUsers() failed", e)
            Result.failure(e)
        }
    }
}