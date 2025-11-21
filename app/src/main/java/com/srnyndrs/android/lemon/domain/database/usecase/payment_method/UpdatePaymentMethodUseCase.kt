package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import javax.inject.Inject

class UpdatePaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(paymentMethod: PaymentMethod): Result<Unit> {
        return paymentMethodRepository.updatePaymentMethod(paymentMethod)
    }
}