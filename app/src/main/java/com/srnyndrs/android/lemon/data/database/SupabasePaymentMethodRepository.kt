package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class SupabasePaymentMethodRepository @Inject constructor(
    private val client: SupabaseClient
): PaymentMethodRepository {
    override suspend fun getPaymentMethods(householdId: String): List<PaymentMethod> {
        try {
            val response = client
                .from(table = DatabaseView.PAYMENT_METHODS.path)
                .select {
                    filter { PaymentMethodDto::household_id eq householdId }
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

}