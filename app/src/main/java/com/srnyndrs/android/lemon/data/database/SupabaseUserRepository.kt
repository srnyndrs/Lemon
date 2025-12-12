package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.UserDto
import com.srnyndrs.android.lemon.data.database.dto.UserWithHousehold
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.domain.database.UserRepository
import com.srnyndrs.android.lemon.domain.database.model.UserMainData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
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
            val user = response.toDomain(client.supabaseHttpUrl)
            Log.d(_tag, "getUser() returned: $user")
            return Result.success(user)
        } catch (e: Exception) {
            Log.e(_tag, "getUser() failed", e)
            return Result.failure(e)
        }
    }

    override suspend fun getUsers(currentUserId: String): Result<List<Pair<String, String>>> {
        Log.d(_tag, "getUsers() called")
        return try {
            val response = client
                .from(DatabaseEndpoint.USERS_VIEW.path)
                .select()
                .decodeList<UserDto>()
            val users = response.map { it.id to it.username }.filter { it.first != currentUserId }
            Log.d(_tag, "getUsers() returned: $users")
            Result.success(users)
        } catch (e: Exception) {
            Log.e(_tag, "getUsers() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateUsername(userId: String, newUsername: String): Result<Unit> {
        Log.d(_tag, "updateUsername() called with: userId = $userId, newUsername = $newUsername")
        return try {
            client.postgrest.rpc(
                function = "update_username",
                parameters = mapOf(
                    "p_user_id" to userId,
                    "p_new_username" to newUsername
                )
            )
            Log.d(_tag, "updateUsername() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "updateUsername() failed", e)
            Result.failure(e)
        }
    }
}