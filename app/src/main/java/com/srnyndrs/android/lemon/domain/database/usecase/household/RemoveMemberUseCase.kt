package com.srnyndrs.android.lemon.domain.database.usecase.household

import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import javax.inject.Inject

class RemoveMemberUseCase @Inject constructor(
    private val householdRepository: HouseholdRepository
) {
    suspend operator fun invoke(householdId: String, userId: String): Result<Unit> {
        return householdRepository.removeMember(householdId, userId)
    }
}