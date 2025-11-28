package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabasePaymentMethodRepository @Inject constructor(
    private val client: SupabaseClient
): PaymentMethodRepository {

    private val _tag = "SupabasePaymentMethodRepo"

    override suspend fun getPaymentMethods(householdId: String, userId: String?): Result<List<PaymentMethod>> {
        Log.d(_tag, "getPaymentMethods() called with: householdId = $householdId, userId = $userId")
        return try {
            val response = client
                .from(table = DatabaseEndpoint.PAYMENT_METHODS_VIEW.path)
                .select {
                    filter {
                        or {
                            PaymentMethodDto::householdId eq householdId
                            userId?.let {
                                PaymentMethodDto::ownerUserId eq it
                            }
                        }
                    }
                }
                .decodeList<PaymentMethodDto>()

            // Deduplicate payment methods by id. If a payment method appears multiple times
            // (e.g. it's linked to multiple households and also owned by the user), prefer
            // the row that matches the requested `householdId` so `inHousehold` is correct.
            val uniqueDtos = response
                .groupBy { it.id }
                .mapValues { (_, list) -> list.find { it.householdId == householdId } ?: list.first() }
                .values
                .toList()

            val methods = uniqueDtos.map { it.toDomain(householdId, userId) }
                .sortedByDescending { it.inHousehold }
            Log.d(_tag, "getPaymentMethods() returned: $methods")
            Result.success(methods)
        } catch (e: Exception) {
            Log.e(_tag, "getPaymentMethods() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun addPaymentMethod(paymentMethod: PaymentMethod, householdId: String, userId: String): Result<PaymentMethod> {
        Log.d(_tag, "addPaymentMethod() called with: paymentMethod = $paymentMethod, householdId = $householdId, userId = $userId")
        return try {
            client.postgrest.rpc(
                function = DatabaseEndpoint.ADD_PAYMENT_FUNCTION.path,
                parameters = buildJsonObject {
                    put("p_user_id", userId)
                    put("p_household_id", householdId)
                    put("p_name", paymentMethod.name)
                    put("p_icon", paymentMethod.icon)
                    put("p_color", paymentMethod.color)
                    put("p_type", paymentMethod.type.lowercase())
                }
            )
            Log.d(_tag, "addPaymentMethod() returned: $paymentMethod")
            Result.success(paymentMethod)
        } catch (e: Exception) {
            Log.e(_tag, "addPaymentMethod() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit> {
        Log.d(_tag, "updatePaymentMethod() called with: paymentMethod = $paymentMethod")
        return try {
            client.postgrest.rpc(
                function = "update_payment_method",
                parameters = buildJsonObject {
                    put("p_payment_method_id", paymentMethod.id)
                    put("p_name", paymentMethod.name)
                    put("p_icon", paymentMethod.icon)
                    put("p_color", paymentMethod.color)
                    put("p_type", paymentMethod.type.lowercase())
                    put("p_is_active", paymentMethod.isActive)
                }
            )
            Log.d(_tag, "updatePaymentMethod() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "updatePaymentMethod() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deletePaymentMethod(paymentMethodId: String): Result<Unit> {
        Log.d(_tag, "deletePaymentMethod() called with: paymentMethodId = $paymentMethodId")
        return try {
            client.postgrest.rpc(
                function = "delete_payment_method",
                parameters = buildJsonObject {
                    put("p_payment_method_id", paymentMethodId)
                }
            )
            Log.d(_tag, "deletePaymentMethod() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "deletePaymentMethod() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun linkPaymentMethodToHousehold(paymentMethodId: String, householdId: String): Result<Unit> {
        Log.d(_tag, "linkPaymentMethodToHousehold() called with: paymentMethodId = $paymentMethodId, householdId = $householdId")
        return try {
            client.postgrest.rpc(
                function = DatabaseEndpoint.LINK_PAYMENT_FUNCTION.path,
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_payment_method_id", paymentMethodId)
                }
            )
            Log.d(_tag, "linkPaymentMethodToHousehold() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "linkPaymentMethodToHousehold() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun unlinkPaymentMethodFromHousehold(paymentMethodId: String, householdId: String): Result<Unit> {
        Log.d(_tag, "unlinkPaymentMethodFromHousehold() called with: paymentMethodId = $paymentMethodId, householdId = $householdId")
        return try {
            client.postgrest.rpc(
                function = DatabaseEndpoint.UNLINK_PAYMENT_FUNCTION.path,
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_payment_method_id", paymentMethodId)
                }
            )
            Log.d(_tag, "unlinkPaymentMethodFromHousehold() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "unlinkPaymentMethodFromHousehold() failed", e)
            Result.failure(e)
        }
    }
}
