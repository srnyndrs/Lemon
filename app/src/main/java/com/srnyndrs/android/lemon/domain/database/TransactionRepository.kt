package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem

interface TransactionRepository {
    suspend fun getTransactions(
        householdId: String,
        year: Int? = null,
        month: Int? = null,
    ): Result<Map<String, List<TransactionItem>>>
    suspend fun getMonthlyStats(
        householdId: String,
        year: Int? = null,
        month: Int? = null,
    ): Result<List<StatisticGroupItem>>

    suspend fun getStatistics(householdId: String): Result<List<Pair<Int, Double>>>

    suspend fun addTransaction(
        householdId: String,
        userId: String,
        transactionDetailsDto: TransactionDetailsDto
    ): Result<Transaction>

    suspend fun getTransactionsByPaymentMethod(
        householdId: String,
        paymentMethodId: String
    ): Result<Map<String, List<TransactionItem>>>

    suspend fun deleteTransaction(transactionId: String): Result<Unit>

}