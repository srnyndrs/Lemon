package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

interface PaymentMethodRepository {
    suspend fun getPaymentMethods(householdId: String): List<PaymentMethod>
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod, householdId: String, userId: String): Result<PaymentMethod>
}