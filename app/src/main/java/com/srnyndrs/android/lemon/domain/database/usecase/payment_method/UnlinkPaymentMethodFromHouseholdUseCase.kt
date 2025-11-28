package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import javax.inject.Inject

class UnlinkPaymentMethodFromHouseholdUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(householdId: String, paymentMethodId: String) =
        paymentMethodRepository.unlinkPaymentMethodFromHousehold(paymentMethodId = paymentMethodId, householdId = householdId)
}
