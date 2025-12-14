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

    private val _tag = "SupabaseHouseholdRepo"

    override suspend fun createHousehold(name: String): Result<String> {
        Log.d(_tag, "createHousehold() called with: name = $name")
        return try {
            val result = client.postgrest.rpc(
                function = "handle_new_user_private_household",
                parameters = buildJsonObject {
                    put("p_name", name)
                }
            ).data
            Log.d(_tag, "createHousehold() returned: $result")
            Result.success(result)
        } catch (e: Exception) {
            Log.e(_tag, "createHousehold() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getHousehold(householdId: String): Result<Household> {
        Log.d(_tag, "getHousehold() called with: householdId = $householdId")
        return try {
            val result = client.postgrest.rpc(
                function = "get_household_details",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                }
            ).decodeSingle<HouseholdDto>()
            val household = result.toDomain(client.supabaseHttpUrl)
            Log.d(_tag, "getHousehold() returned: $household")
            Result.success(household)
        } catch (e: Exception) {
            Log.e(_tag, "getHousehold() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun addMember(householdId: String, userId: String, role: String): Result<Unit> {
        Log.d(_tag, "addMember() called with: householdId = $householdId, userId = $userId, role = $role")
        return try {
            client.postgrest.rpc(
                function = "add_household_member",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_user_id", userId)
                    put("p_role", role)
                }
            )
            Log.d(_tag, "addMember() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "addMember() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun removeMember(householdId: String, userId: String): Result<Unit> {
        Log.d(_tag, "removeMember() called with: householdId = $householdId, userId = $userId")
        return try {
            client.postgrest.rpc(
                function = "remove_household_member",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_user_id", userId)
                }
            )
            Log.d(_tag, "removeMember() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "removeMember() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateMemberRole(householdId: String, userId: String, role: String): Result<Unit> {
        Log.d(_tag, "updateMemberRole() called with: householdId = $householdId, userId = $userId, role = $role")
        return try {
            client.postgrest.rpc(
                function = "update_household_member_role",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_user_id", userId)
                    put("p_new_role", role)
                }
            )
            Log.d(_tag, "updateMemberRole() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "updateMemberRole() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateHouseholdName(householdId: String, name: String): Result<Unit> {
        Log.d(_tag, "updateHouseholdName() called with: householdId = $householdId, name = $name")
        return try {
            client.postgrest.rpc(
                function = "update_household_name",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_new_name", name)
                }
            )
            Log.d(_tag, "updateHouseholdName() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "updateHouseholdName() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteHousehold(householdId: String): Result<Unit> {
        Log.d(_tag, "deleteHousehold() called with: householdId = $householdId")
        return try {
            client.postgrest.rpc(
                function = "delete_household",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                }
            )
            Log.d(_tag, "deleteHousehold() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "deleteHousehold() failed", e)
            Result.failure(e)
        }
    }
}
