package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.MonthlyExpenses
import com.srnyndrs.android.lemon.data.database.dto.TransactionDto
import com.srnyndrs.android.lemon.data.database.dto.TransactionStatsDto
import com.srnyndrs.android.lemon.data.database.dto.TransactionsView
import com.srnyndrs.android.lemon.data.mapper.toDomain
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
    override suspend fun getTransactions(householdId: String, year: Int?, month: Int?): Result<Map<String, List<TransactionItem>>> {
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

            Result.success(
                response.map { it.toDomain() }
                    .groupBy { it.date }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthlyStats(householdId: String, year: Int?, month: Int?): Result<List<StatisticGroupItem>> {
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

            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStatistics(householdId: String): Result<List<Pair<Int, Double>>> {
        return try {
            val response = client.from(table = "household_monthly_expenses_view")
                .select {
                    filter {
                        MonthlyExpenses::householdId eq householdId
                    }
                }
                .decodeList<MonthlyExpenses>()

            Result.success(response.map { it.month to it.totalExpenses })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTransactionsByPaymentMethod(householdId: String, paymentMethodId: String): Result<Map<String, List<TransactionItem>>> {
        return try {
            val response = client.from(table = "household_transactions_view") // TODO: proper constant
                .select {
                    filter {
                        TransactionsView::householdId eq householdId
                        TransactionsView::paymentMethodId eq paymentMethodId
                    }
                }
                .decodeList<TransactionsView>()

            Log.d("TransactionRepository", "getTransactionsByPaymentMethod: $paymentMethodId")

            Result.success(
                response.map { it.toDomain() }
                    .groupBy { it.date }
            )
        } catch (e: Exception) {
            Log.e("TransactionRepository", "getTransactionsByPaymentMethod: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun addTransaction(
        householdId: String,
        userId: String,
        transactionDetailsDto: TransactionDetailsDto
    ): Result<Transaction> {
        return try {
            val response = client.from(table = "transactions")
                .insert(
                    value = transactionDetailsDto.toDto(householdId, userId)
                ) {
                    select()
                }.decodeSingle<TransactionDto>()

            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        return try {
            client.postgrest.rpc(
                function = "delete_transaction",
                parameters = mapOf("p_transaction_id" to transactionId)
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}