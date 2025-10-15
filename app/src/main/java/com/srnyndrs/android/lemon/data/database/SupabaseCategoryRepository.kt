package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.CategoryDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import com.srnyndrs.android.lemon.domain.database.model.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class SupabaseCategoryRepository @Inject constructor(
    private val client: SupabaseClient
): CategoryRepository {
    override suspend fun getCategories(householdId: String): List<Category> {
        Log.d("SupabaseCategoryRepo", "getCategories: ${householdId}", )
        try {
            val response = client
                .from(table = DatabaseView.CATEGORIES.path)
                .select {
                    filter { CategoryDto::household_id eq householdId }
                }
                .decodeList<CategoryDto>()
            Log.d("SupabaseCategoryRepo", "getCategories: ${response.size}", )
            return response.map { it.toDomain() }
        } catch (e: Exception) {
            // TODO
            Log.d("SupabaseCategoryRepo", "getCategories: ${e.message}", )
            return emptyList()
        }
    }
}