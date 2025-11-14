package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem

interface TransactionRepository {
    suspend fun getTransactions(householdId: String): Result<Map<String, List<TransactionItem>>>
    suspend fun getMonthlyStats(householdId: String): Result<List<StatisticGroupItem>>
    suspend fun addTransaction(
        householdId: String,
        userId: String,
        transactionDetailsDto: TransactionDetailsDto
    ): Result<Transaction>
}