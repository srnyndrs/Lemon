package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import javax.inject.Inject

class GetPaymentMethodsUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(householdId: String) =
        paymentMethodRepository.getPaymentMethods(householdId)
}