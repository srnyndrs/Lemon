package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.MonthlyExpenses
import com.srnyndrs.android.lemon.data.database.dto.TransactionDto
import com.srnyndrs.android.lemon.data.database.dto.TransactionStatsDto
import com.srnyndrs.android.lemon.data.database.dto.TransactionsView
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.data.mapper.toDomainDto
import com.srnyndrs.android.lemon.data.mapper.toDto
import com.srnyndrs.android.lemon.domain.database.TransactionRepository
import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject

class SupabaseTransactionRepository @Inject constructor(
    private val client: SupabaseClient
): TransactionRepository {

    private val _tag = "SupabaseTransactionRepo"

    override suspend fun getTransactions(householdId: String, year: Int?, month: Int?): Result<Map<String, List<TransactionItem>>> {
        Log.d(_tag, "getTransactions() called with: householdId = $householdId, year = $year, month = $month")
        return try {
            val response = client.from(table = "household_transactions_view") // TODO: proper constant
                .select {
                    filter {
                        TransactionsView::householdId eq householdId
                        year?.let { TransactionsView::year eq it }
                        month?.let { TransactionsView::month eq it }
                    }
                }
                .decodeList<TransactionsView>()
            val transactions = response.map { it.toDomain() }.groupBy { it.date }
            Log.d(_tag, "getTransactions() returned: $transactions")
            Result.success(transactions)
        } catch (e: Exception) {
            Log.e(_tag, "getTransactions() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getMonthlyStats(householdId: String, year: Int?, month: Int?): Result<List<StatisticGroupItem>> {
        Log.d(_tag, "getMonthlyStats() called with: householdId = $householdId, year = $year, month = $month")
        return try {
            val response = client.from(table = "household_summary_view") // TODO: proper constant
                .select {
                    filter {
                        TransactionStatsDto::householdId eq householdId
                        year?.let { TransactionsView::year eq it }
                        month?.let { TransactionsView::month eq it }
                    }
                }
                .decodeList<TransactionStatsDto>()
            val stats = response.map { it.toDomain() }
            Log.d(_tag, "getMonthlyStats() returned: $stats")
            Result.success(stats)
        } catch (e: Exception) {
            Log.e(_tag, "getMonthlyStats() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getStatistics(householdId: String): Result<List<Pair<Int, Double>>> {
        Log.d(_tag, "getStatistics() called with: householdId = $householdId")
        return try {
            val response = client.from(table = "household_monthly_expenses_view")
                .select {
                    filter {
                        MonthlyExpenses::householdId eq householdId
                    }
                }
                .decodeList<MonthlyExpenses>()
            val statistics = response.map { it.month to it.totalExpenses }
            Log.d(_tag, "getStatistics() returned: $statistics")
            Result.success(statistics)
        } catch (e: Exception) {
            Log.e(_tag, "getStatistics() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getIncomeStatistics(householdId: String): Result<List<Pair<Int, Double>>> {
        Log.d(_tag, "getIncomeStatistics() called with: householdId = $householdId")
        return try {
            val response = client.from(table = "household_monthly_income_view")
                .select {
                    filter {
                        com.srnyndrs.android.lemon.data.database.dto.MonthlyIncome::householdId eq householdId
                    }
                }
                .decodeList<com.srnyndrs.android.lemon.data.database.dto.MonthlyIncome>()
            val statistics = response.map { it.month to it.totalIncome }
            Log.d(_tag, "getIncomeStatistics() returned: $statistics")
            Result.success(statistics)
        } catch (e: Exception) {
            Log.e(_tag, "getIncomeStatistics() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getTransactionsByPaymentMethod(householdId: String, paymentMethodId: String): Result<Map<String, List<TransactionItem>>> {
        Log.d(_tag, "getTransactionsByPaymentMethod() called with: householdId = $householdId, paymentMethodId = $paymentMethodId")
        return try {
            val response = client.from(table = "household_transactions_view") // TODO: proper constant
                .select {
                    filter {
                        TransactionsView::householdId eq householdId
                        TransactionsView::paymentMethodId eq paymentMethodId
                    }
                }
                .decodeList<TransactionsView>()
            val transactions = response.map { it.toDomain() }.groupBy { it.date }
            Log.d(_tag, "getTransactionsByPaymentMethod() returned: $transactions")
            Result.success(transactions)
        } catch (e: Exception) {
            Log.e(_tag, "getTransactionsByPaymentMethod() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun addTransaction(
        householdId: String,
        userId: String,
        transactionDetailsDto: TransactionDetailsDto
    ): Result<Transaction> {
        Log.d(_tag, "addTransaction() called with: householdId = $householdId, userId = $userId, transactionDetailsDto = $transactionDetailsDto")
        return try {
            val response = client.from(table = "transactions")
                .insert(
                    value = transactionDetailsDto.toDto(householdId, userId)
                ) {
                    select()
                }.decodeSingle<TransactionDto>()
            val transaction = response.toDomain()
            Log.d(_tag, "addTransaction() returned: $transaction")
            Result.success(transaction)
        } catch (e: Exception) {
            Log.e(_tag, "addTransaction() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(
        householdId: String,
        userId: String,
        transactionId: String,
        transactionDetailsDto: TransactionDetailsDto
    ): Result<Transaction> {
        Log.d(_tag, "updateTransaction() called with: householdId = $householdId, userId = $userId, transactionId = $transactionId, transactionDetailsDto = $transactionDetailsDto")
        return try {
            val response = client.from(table = "transactions")
                .update(
                    value = transactionDetailsDto.toDto(householdId, userId)
                ) {
                    filter {
                        TransactionDto::id eq transactionId
                        TransactionDto::householdId eq householdId
                    }
                    select()
                }
                .decodeSingle<TransactionDto>()
            val transaction = response.toDomain()
            Log.d(_tag, "updateTransaction() returned: $transaction")
            Result.success(transaction)
        } catch (e: Exception) {
            Log.e(_tag, "updateTransaction() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getTransactionById(
        householdId: String,
        transactionId: String
    ): Result<TransactionDetailsDto> {
        Log.d(_tag, "getTransactionById() called with: householdId = $householdId, transactionId = $transactionId")
        return try {
            val response = client.from(table = "transactions")
                .select {
                    filter {
                        TransactionDto::householdId eq householdId
                        TransactionDto::id eq transactionId
                    }
                }
                .decodeList<TransactionDto>()
            val transaction = response.first().toDomainDto()
            Log.d(_tag, "getTransactionById() returned: $transaction")
            Result.success(transaction)
        } catch (e: Exception) {
            Log.e(_tag, "getTransactionById() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        Log.d(_tag, "deleteTransaction() called with: transactionId = $transactionId")
        return try {
            client.postgrest.rpc(
                function = "delete_transaction",
                parameters = mapOf("p_transaction_id" to transactionId)
            )
            Log.d(_tag, "deleteTransaction() returned: Unit")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(_tag, "deleteTransaction() failed", e)
            Result.failure(e)
        }
    }
}