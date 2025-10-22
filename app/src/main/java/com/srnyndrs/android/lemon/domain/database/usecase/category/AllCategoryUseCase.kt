package com.srnyndrs.android.lemon.domain.database.usecase.category

import javax.inject.Inject

class AllCategoryUseCase @Inject constructor(
    val addCategoryUseCase: AddCategoryUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase
)