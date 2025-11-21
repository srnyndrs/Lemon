package com.srnyndrs.android.lemon.domain.database.usecase.household

import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import com.srnyndrs.android.lemon.domain.database.model.Household
import javax.inject.Inject

class GetHouseholdUseCase @Inject constructor(
    private val householdRepository: HouseholdRepository
) {
    suspend operator fun invoke(householdId: String): Result<Household> {
        return householdRepository.getHousehold(householdId)
    }
}