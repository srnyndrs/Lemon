package com.srnyndrs.android.lemon.domain.database.usecase.category

import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: String): Result<Unit> {
        return categoryRepository.deleteCategory(categoryId)
    }
}