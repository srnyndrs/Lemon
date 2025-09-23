package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.domain.database.UserRepository
import com.srnyndrs.android.lemon.domain.database.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class SupabaseUserRepository @Inject constructor(
    private val client: SupabaseClient
): UserRepository {
    override suspend fun getUser(userId: String): Result<User> {
        try {
            val user = client
                .from("user_with_households")
                .select() {
                    filter { User::user_id eq userId }
                }
                .decodeSingle<User>()
            return Result.success(user)
        } catch (e: Exception) {
            Log.d("SupabaseUserRepository", "${e.message}")
            return Result.failure(e)
        }
    }

}