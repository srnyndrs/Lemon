package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

interface PaymentMethodRepository {
    suspend fun getPaymentMethods(householdId: String): List<PaymentMethod>
}