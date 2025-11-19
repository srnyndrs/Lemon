package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseHouseholdRepository @Inject constructor(
    private val client: SupabaseClient
) : HouseholdRepository {
    override suspend fun createHousehold(name: String): Result<String> {
        return try {
            val result = client.postgrest.rpc(
                function = "handle_new_user_private_household", // TODO: proper constant
                parameters = buildJsonObject {
                    put("p_name", name)
                }
            ).data
            Log.d("SupabaseHouseholdRepo", "success:",)
            Result.success(result)
        } catch (e: Exception) {
            Log.d("SupabaseHouseholdRepo", "error: ", e)
            Result.failure(e)
        }
    }
}
