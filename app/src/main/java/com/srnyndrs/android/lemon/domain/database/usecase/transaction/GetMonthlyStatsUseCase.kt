package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import javax.inject.Inject

class GetMonthlyStatsUseCase @Inject constructor(
   private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(householdId: String): Result<List<StatisticGroupItem>> {
        return transactionRepository.getMonthlyStats(householdId)
    }
}