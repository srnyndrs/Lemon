package com.srnyndrs.android.lemon.ui.screen.main.content.insights

import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.ui.utils.UiState

data class InsightsState(
    val statistics: UiState<List<StatisticGroupItem>> = UiState.Empty(),
    val allExpenses: UiState<List<Pair<Int, Double>>> = UiState.Empty(),
    val allIncomes: UiState<List<Pair<Int, Double>>> = UiState.Empty()
)
