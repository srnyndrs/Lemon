package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.HouseholdDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import com.srnyndrs.android.lemon.domain.database.model.Household
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
                function = "handle_new_user_private_household",
                parameters = buildJsonObject {
                    put("p_name", name)
                }
            ).data
            Result.success(result)
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error creating household", e)
            Result.failure(e)
        }
    }

    override suspend fun getHousehold(householdId: String): Result<Household> {
        return try {
            val result = client.postgrest.rpc(
                function = "get_household_details",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                }
            ).decodeSingle<HouseholdDto>()
            Result.success(result.toDomain())
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error getting household", e)
            Result.failure(e)
        }
    }

    override suspend fun addMember(householdId: String, userId: String, role: String): Result<Unit> {
        return try {
            client.postgrest.rpc(
                function = "add_household_member",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_user_id", userId)
                    put("p_role", role)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error adding member", e)
            Result.failure(e)
        }
    }

    override suspend fun removeMember(householdId: String, userId: String): Result<Unit> {
        return try {
            client.postgrest.rpc(
                function = "remove_household_member",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_user_id", userId)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error removing member", e)
            Result.failure(e)
        }
    }

    override suspend fun updateMemberRole(householdId: String, userId: String, role: String): Result<Unit> {
        return try {
            client.postgrest.rpc(
                function = "update_household_member_role",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_user_id", userId)
                    put("p_new_role", role)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error updating member role", e)
            Result.failure(e)
        }
    }

    override suspend fun updateHouseholdName(householdId: String, name: String): Result<Unit> {
        return try {
            client.postgrest.rpc(
                function = "update_household_name",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_new_name", name)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error updating household name", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteHousehold(householdId: String): Result<Unit> {
        return try {
            client.postgrest.rpc(
                function = "delete_household",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SupabaseHouseholdRepo", "Error deleting household", e)
            Result.failure(e)
        }
    }
}
