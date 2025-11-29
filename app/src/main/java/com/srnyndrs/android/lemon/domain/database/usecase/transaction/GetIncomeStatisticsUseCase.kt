package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import javax.inject.Inject

class GetIncomeStatisticsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(householdId: String) = transactionRepository.getIncomeStatistics(householdId)
}
