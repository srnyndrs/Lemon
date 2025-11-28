package com.srnyndrs.android.lemon.ui.screen.main.content.transactions

import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.ui.utils.UiState

data class TransactionState(
    val transactions: UiState<Map<String, List<TransactionItem>>> = UiState.Empty(),
    val selectedMonth: Int = 1,
    val selectedYear: Int = 1970,
)
