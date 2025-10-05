package com.srnyndrs.android.lemon.data.database

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
                .from(DatabaseView.USER_HOUSEHOLDS.path)
                .select {
                    filter { UserWithHousehold::user_id eq userId }
                }
                .decodeList<UserWithHousehold>()

            return Result.success(response.toDomain())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}