package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.Category

interface CategoryRepository {
    suspend fun getCategories(householdId: String): Result<List<Category>>
    suspend fun addCategory(category: Category, householdId: String): Result<Category>
    suspend fun deleteCategory(categoryId: String): Result<Unit>
    suspend fun updateCategory(category: Category): Result<Unit>
}