package com.srnyndrs.android.lemon.data.database

import android.util.Log
import com.srnyndrs.android.lemon.data.database.dto.CategoryDto
import com.srnyndrs.android.lemon.data.mapper.toDomain
import com.srnyndrs.android.lemon.data.mapper.toDto
import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import com.srnyndrs.android.lemon.domain.database.model.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseCategoryRepository @Inject constructor(
    private val client: SupabaseClient
): CategoryRepository {

    private val _tag = "SupabaseCategoryRepo"

    override suspend fun getCategories(householdId: String): Result<List<Category>> {
        Log.d(_tag, "getCategories() called with: householdId = $householdId")
        return try {
            val response = client
                .from(table = DatabaseEndpoint.CATEGORIES_TABLE.path)
                .select {
                    filter { CategoryDto::householdId eq householdId }
                }
                .decodeList<CategoryDto>()
            val categories = response.map { it.toDomain() }.sortedBy { it.name }
            Log.d(_tag, "getCategories() returned: $categories")
            Result.success(categories)
        } catch (e: Exception) {
            Log.e(_tag, "getCategories() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun addCategory(category: Category, householdId: String): Result<Category> {
        Log.d(_tag, "addCategory() called with: category = $category, householdId = $householdId")
        return try {
            val response = client
                .from(table = DatabaseEndpoint.CATEGORIES_TABLE.path)
                .insert(
                    value = category.toDto(householdId)
                ) {
                    select()
                }.decodeSingle<CategoryDto>()
            val newCategory = response.toDomain()
            Log.d(_tag, "addCategory() returned: $newCategory")
            Result.success(newCategory)
        } catch (e: Exception) {
            Log.e(_tag, "addCategory() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(categoryId: String): Result<String> {
        Log.d(_tag, "deleteCategory() called with: categoryId = $categoryId")
        return try {
            client.postgrest.rpc(
                function = "delete_category",
                parameters = buildJsonObject {
                    put("p_category_id", categoryId)
                }
            )
            Log.d(_tag, "deleteCategory() returned: Unit")
            Result.success(categoryId)
        } catch (e: Exception) {
            Log.e(_tag, "deleteCategory() failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateCategory(category: Category): Result<String> {
        Log.d(_tag, "updateCategory() called with: category = $category")
        return try {
            client.postgrest.rpc(
                function = "update_category",
                parameters = buildJsonObject {
                    put("p_category_id", category.id)
                    put("p_name", category.name)
                    put("p_icon", category.icon)
                    put("p_color", category.color)
                }
            )
            Log.d(_tag, "updateCategory() returned: Unit")
            Result.success(category.id!!)
        } catch (e: Exception) {
            Log.e(_tag, "updateCategory() failed", e)
            Result.failure(e)
        }
    }
}