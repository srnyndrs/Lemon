package com.srnyndrs.android.lemon.data.database

import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.data.mapper.toDto
import com.srnyndrs.android.lemon.data.mapper.toJsonObject
import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class SupabasePaymentMethodRepository @Inject constructor(
    private val client: SupabaseClient
): PaymentMethodRepository {
    override suspend fun getPaymentMethods(householdId: String): Result<List<PaymentMethod>> {
        return try {
            val response = client
                .from(table = DatabaseEndpoint.PAYMENT_METHODS_VIEW.path)
                .select {
                    filter { PaymentMethodDto::householdId eq householdId }
                }
                .decodeList<PaymentMethodDto>()

            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addPaymentMethod(paymentMethod: PaymentMethod, householdId: String, userId: String): Result<PaymentMethod> {
        return try {
            val dto = paymentMethod.toDto(householdId, userId)
            client.postgrest.rpc(
                function = DatabaseEndpoint.ADD_PAYMENT_FUNCTION.path,
                parameters = dto.toJsonObject(householdId, userId)
            )

            Result.success(paymentMethod)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}