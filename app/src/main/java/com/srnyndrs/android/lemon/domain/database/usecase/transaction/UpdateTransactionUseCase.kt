package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        householdId: String,
        userId: String,
        transactionId: String,
        transactionDetailsDto: TransactionDetailsDto
    ) = transactionRepository.updateTransaction(
        householdId = householdId,
        userId = userId,
        transactionId = transactionId,
        transactionDetailsDto = transactionDetailsDto
    )
}
