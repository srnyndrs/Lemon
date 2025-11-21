package com.srnyndrs.android.lemon.domain.database.usecase.household

import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import javax.inject.Inject

class DeleteHouseholdUseCase @Inject constructor(
    private val householdRepository: HouseholdRepository
) {
    suspend operator fun invoke(householdId: String): Result<Unit> {
        return householdRepository.deleteHousehold(householdId)
    }
}