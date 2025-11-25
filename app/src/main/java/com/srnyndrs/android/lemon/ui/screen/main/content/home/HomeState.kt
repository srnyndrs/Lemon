package com.srnyndrs.android.lemon.ui.screen.main.content.home

import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.ui.utils.UiState

data class HomeState(
    val transactions: UiState<Map<String, List<TransactionItem>>> = UiState.Empty(),
    val expenses: UiState<Map<TransactionType, Double>> = UiState.Empty(),
)
