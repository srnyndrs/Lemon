package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import com.srnyndrs.android.lemon.domain.database.PaymentMethodRepository
import javax.inject.Inject

class LinkPaymentMethodToHouseholdUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    suspend operator fun invoke(householdId: String, paymentMethodId: String) =
        paymentMethodRepository.linkPaymentMethodToHousehold(paymentMethodId = paymentMethodId, householdId = householdId)
}
