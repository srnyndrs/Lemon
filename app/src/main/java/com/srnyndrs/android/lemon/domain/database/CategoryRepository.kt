package com.srnyndrs.android.lemon.domain.database

import com.srnyndrs.android.lemon.domain.database.model.Category

interface CategoryRepository {
    suspend fun getCategories(householdId: String): List<Category>
    suspend fun addCategory(category: Category, householdId: String): Result<Category>
}