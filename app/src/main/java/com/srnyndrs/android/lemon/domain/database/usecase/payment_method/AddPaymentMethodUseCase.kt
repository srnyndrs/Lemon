package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import javax.inject.Inject

class AddPaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(
        paymentMethod: PaymentMethod,
        householdId: String,
        userId: String
    ) = paymentMethodRepository.addPaymentMethod(
        paymentMethod = paymentMethod,
        householdId = householdId,
        userId = userId
    )
}