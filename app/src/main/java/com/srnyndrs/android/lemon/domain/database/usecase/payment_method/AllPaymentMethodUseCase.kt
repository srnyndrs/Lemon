package com.srnyndrs.android.lemon.domain.database.usecase.payment_method

import javax.inject.Inject

class AllPaymentMethodUseCase @Inject constructor(
    val addPaymentMethodUseCase: AddPaymentMethodUseCase,
    val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
)