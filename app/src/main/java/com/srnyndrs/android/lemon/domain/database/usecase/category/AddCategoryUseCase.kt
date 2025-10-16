package com.srnyndrs.android.lemon.domain.database.usecase.category

import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import com.srnyndrs.android.lemon.domain.database.model.Category
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category, householdId: String) =
        categoryRepository.addCategory(category, householdId)
}