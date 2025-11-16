package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: String): Result<Unit> {
        return transactionRepository.deleteTransaction(transactionId)
    }
}
