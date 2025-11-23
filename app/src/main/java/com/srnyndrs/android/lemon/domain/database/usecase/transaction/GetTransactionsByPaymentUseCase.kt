package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import javax.inject.Inject

class GetTransactionsByPaymentUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(householdId: String, paymentMethodId: String)
        = transactionRepository.getTransactionsByPaymentMethod(householdId, paymentMethodId)
}