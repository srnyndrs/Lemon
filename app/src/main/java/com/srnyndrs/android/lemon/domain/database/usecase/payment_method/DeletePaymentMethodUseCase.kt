package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import javax.inject.Inject

class DeletePaymentMethodUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(paymentMethodId: String): Result<Unit> {
        return paymentMethodRepository.deletePaymentMethod(paymentMethodId)
    }
}