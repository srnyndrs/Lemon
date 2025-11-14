package com.srnyndrs.android.lemon.data.database

import android.util.Log
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
import javax.inject.Inject

class SupabaseTransactionRepository @Inject constructor(
    private val client: SupabaseClient
): TransactionRepository {
    override suspend fun getTransactions(householdId: String): Result<Map<String, List<TransactionItem>>> {
        return try {
            val response = client.from(table = "household_transactions_view")
                .select {
                    filter { TransactionsView::householdId eq householdId }
                }
                .decodeList<TransactionsView>()

            Result.success(response.map { it.toDomain() }.groupBy { it.date })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthlyStats(householdId: String): Result<List<StatisticGroupItem>> {
        return try {
            val response = client.from(table = "household_summary_view")
                .select {
                    filter { TransactionsView::householdId eq householdId }
                }
                .decodeList<TransactionStatsDto>()

            Log.d("SupabaseTransactionRepo", "Fetched monthly stats: $response")
            Result.success(response.map{ it.toDomain() })
        } catch (e: Exception) {
            Log.d("SupabaseTransactionRepo", "Error fetching monthly stats", e)
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
}