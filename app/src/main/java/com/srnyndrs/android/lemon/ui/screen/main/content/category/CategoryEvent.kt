package com.srnyndrs.android.lemon.ui.screen.main.content.category

import com.srnyndrs.android.lemon.domain.database.model.Category

sealed class CategoryEvent {
    data class AddCategory(val category: Category) : CategoryEvent()
    data class DeleteCategory(val categoryId: String) : CategoryEvent()
}