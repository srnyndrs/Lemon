package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

interface PaymentMethodRepository {
    suspend fun getPaymentMethods(householdId: String, userId: String? = null): Result<List<PaymentMethod>>
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod, householdId: String, userId: String): Result<PaymentMethod>
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod): Result<Unit>
    suspend fun deletePaymentMethod(paymentMethodId: String): Result<Unit>
    suspend fun linkPaymentMethodToHousehold(paymentMethodId: String, householdId: String): Result<Unit>
    suspend fun unlinkPaymentMethodFromHousehold(paymentMethodId: String, householdId: String): Result<Unit>
}