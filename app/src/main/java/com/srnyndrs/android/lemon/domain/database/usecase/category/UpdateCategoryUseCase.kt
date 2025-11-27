package com.srnyndrs.android.lemon.domain.database.usecase.category

import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import com.srnyndrs.android.lemon.domain.database.model.Category
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<String> {
        return categoryRepository.updateCategory(category)
    }
}