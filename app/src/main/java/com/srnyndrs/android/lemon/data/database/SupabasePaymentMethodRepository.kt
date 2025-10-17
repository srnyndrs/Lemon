package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.data.mapper.toDto
import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class SupabasePaymentMethodRepository @Inject constructor(
    private val client: SupabaseClient
): PaymentMethodRepository {
    override suspend fun getPaymentMethods(householdId: String): List<PaymentMethod> {
        try {
            val response = client
                .from(table = DatabaseView.PAYMENT_METHODS.path)
                .select {
                    filter { PaymentMethodDto::householdId eq householdId }
                }
                .decodeList<PaymentMethodDto>()
            Log.d("SupabasePaymentMethodRepo", "getPaymentMethods: ${response.size}", )
            return response.map { it.toDomain() }
        } catch (e: Exception) {
            // TODO
            Log.d("SupabasePaymentMethodRepo", "getPaymentMethods: ${e.message}", )
            return emptyList()
        }
    }

    override suspend fun addPaymentMethod(paymentMethod: PaymentMethod, householdId: String, userId: String): Result<PaymentMethod> {
        return try {
            val dto = paymentMethod.toDto(householdId, userId)
            client.postgrest.rpc(
                function = "add_payment_method",
                parameters = JsonObject(
                    mapOf(
                        "p_color" to (dto.color?.let { JsonPrimitive(it) } ?: JsonNull),
                        "p_household_id" to JsonPrimitive(householdId),
                        "p_icon" to (dto.icon?.let { JsonPrimitive(it) } ?: JsonNull),
                        "p_name" to JsonPrimitive(dto.name),
                        "p_type" to JsonPrimitive("card"),
                        "p_user_id" to JsonPrimitive(userId),
                    )
                ),
            )

            Result.success(paymentMethod)
        } catch (e: Exception) {
            Log.d("SupabasePaymentMethodRepo", "addPaymentMethod: ${e.message}", )
            Result.failure(e)
        }
    }

}