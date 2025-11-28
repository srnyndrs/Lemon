package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import com.srnyndrs.android.lemon.data.database.SupabaseTransactionRepository
import javax.inject.Inject

class GetTransactionById @Inject constructor(
    private val transactionRepository: SupabaseTransactionRepository
) {
    suspend operator fun invoke(
        householdId: String,
        transactionId: String
    ) =
        transactionRepository.getTransactionById(
            householdId,
            transactionId
        )
}