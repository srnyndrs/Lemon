package com.srnyndrs.android.lemon.data.database

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
    override suspend fun getUser(userId: String): Result<UserMainData> {
        try {
            val response = client
                .from(DatabaseEndpoint.USER_HOUSEHOLDS_VIEW.path)
                .select {
                    filter { UserWithHousehold::userId eq userId }
                }
                .decodeList<UserWithHousehold>()

            return Result.success(response.toDomain())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<Pair<String, String>>> {
        return try {
            val response = client
                .from(DatabaseEndpoint.USERS_VIEW.path)
                .select()
                .decodeList<UserDto>()
            Result.success(response.map { it.id to it.username })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}