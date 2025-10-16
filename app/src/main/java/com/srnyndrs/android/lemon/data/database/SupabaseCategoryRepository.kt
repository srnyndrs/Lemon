package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.CategoryDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.data.mapper.toDto
import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import com.srnyndrs.android.lemon.domain.database.model.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class SupabaseCategoryRepository @Inject constructor(
    private val client: SupabaseClient
): CategoryRepository {
    override suspend fun getCategories(householdId: String): List<Category> {
        try {
            val response = client
                .from(table = DatabaseView.CATEGORIES.path)
                .select {
                    filter { CategoryDto::household_id eq householdId }
                }
                .decodeList<CategoryDto>()
            return response.map { it.toDomain() }
        } catch (e: Exception) {
            // TODO: handle exceptions
            return emptyList()
        }
    }

    override suspend fun addCategory(category: Category, householdId: String): Result<Category> {
        return try {
            val response = client
                .from(table = DatabaseView.CATEGORIES.path)
                .insert(
                    value = category.toDto(householdId)
                ) {
                    select()
                }.decodeSingle<CategoryDto>()

            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}