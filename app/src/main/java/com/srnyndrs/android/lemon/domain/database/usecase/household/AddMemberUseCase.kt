package com.srnyndrs.android.lemon.domain.database.usecase.household

import com.srnyndrs.android.lemon.domain.database.HouseholdRepository
import javax.inject.Inject

class AddMemberUseCase @Inject constructor(
    private val householdRepository: HouseholdRepository
) {
    suspend operator fun invoke(householdId: String, userId: String, role: String): Result<Unit> {
        return householdRepository.addMember(householdId, userId, role)
    }
}