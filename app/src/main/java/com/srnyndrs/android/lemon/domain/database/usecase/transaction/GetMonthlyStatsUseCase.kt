package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import javax.inject.Inject

class GetMonthlyStatsUseCase @Inject constructor(
   private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(householdId: String) = transactionRepository.getMonthlyStats(householdId)
}