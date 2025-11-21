package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

interface PaymentMethodRepository {
    suspend fun getPaymentMethods(householdId: String): Result<List<PaymentMethod>>
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod, householdId: String, userId: String): Result<PaymentMethod>
    suspend fun deactivatePaymentMethod(paymentMethodId: String): Result<Unit>
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit>
    suspend fun deletePaymentMethod(paymentMethodId: String): Result<Unit>
}