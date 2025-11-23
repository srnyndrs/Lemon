package com.srnyndrs.android.lemon.ui.screen.main.content.category

import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.ui.utils.UiState

data class CategoryState(
    val categories: UiState<List<Category>> = UiState.Empty(),
    val actionProgress: Boolean = false
)
