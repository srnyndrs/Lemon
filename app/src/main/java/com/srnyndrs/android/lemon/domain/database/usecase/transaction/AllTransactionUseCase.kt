package com.srnyndrs.android.lemon.domain.database.usecase.transaction

import javax.inject.Inject

class AllTransactionUseCase @Inject constructor(
    val addTransactionUseCase: AddTransactionUseCase,
    val getMonthlyTransactionsUseCase: GetMonthlyTransactionsUseCase,
    val getMonthlyStatsUseCase: GetMonthlyStatsUseCase,
    val deleteTransactionUseCase: DeleteTransactionUseCase
)