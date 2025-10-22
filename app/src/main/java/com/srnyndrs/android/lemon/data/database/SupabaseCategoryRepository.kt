package com.srnyndrs.android.lemon.data.database

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
    override suspend fun getCategories(householdId: String): Result<List<Category>> {
        return try {
            val response = client
                .from(table = DatabaseEndpoint.CATEGORIES.path)
                .select {
                    filter { CategoryDto::householdId eq householdId }
                }
                .decodeList<CategoryDto>()

            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addCategory(category: Category, householdId: String): Result<Category> {
        return try {
            val response = client
                .from(table = DatabaseEndpoint.CATEGORIES.path)
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