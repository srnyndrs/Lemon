package com.srnyndrs.android.lemon.domain.database.usecase.category

import com.srnyndrs.android.lemon.domain.database.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(householdId: String) = categoryRepository.getCategories(householdId)
}