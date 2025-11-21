package com.srnyndrs.android.lemon.domain.database.usecase.household

import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import javax.inject.Inject

class UpdateHouseholdNameUseCase @Inject constructor(
    private val householdRepository: HouseholdRepository
) {
    suspend operator fun invoke(householdId: String, name: String): Result<Unit> {
        return householdRepository.updateHouseholdName(householdId, name)
    }
}