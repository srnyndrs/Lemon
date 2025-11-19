package com.srnyndrs.android.lemon.domain.database.usecase.household

import javax.inject.Inject

data class AllHouseholdUseCase @Inject constructor(
    val createHouseholdUseCase: CreateHouseholdUseCase
)
