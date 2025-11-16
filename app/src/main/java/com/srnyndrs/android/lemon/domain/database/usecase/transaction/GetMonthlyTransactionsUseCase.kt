package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import javax.inject.Inject

class GetMonthlyTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(householdId: String): Result<Map<String, List<TransactionItem>>> {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1

        return transactionRepository.getTransactions(householdId, currentYear, currentMonth)
    }
}