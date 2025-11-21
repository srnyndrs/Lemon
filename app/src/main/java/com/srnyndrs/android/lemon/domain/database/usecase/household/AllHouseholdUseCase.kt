package com.srnyndrs.android.lemon.domain.database.usecase.household

import javax.inject.Inject

data class AllHouseholdUseCase @Inject constructor(
    val createHouseholdUseCase: CreateHouseholdUseCase,
    val getHouseholdUseCase: GetHouseholdUseCase,
    val addMemberUseCase: AddMemberUseCase,
    val removeMemberUseCase: RemoveMemberUseCase,
    val updateMemberRoleUseCase: UpdateMemberRoleUseCase,
    val updateHouseholdNameUseCase: UpdateHouseholdNameUseCase,
    val deleteHouseholdUseCase: DeleteHouseholdUseCase
)
