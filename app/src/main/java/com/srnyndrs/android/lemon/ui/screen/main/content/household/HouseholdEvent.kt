package com.srnyndrs.android.lemon.ui.screen.main.content.household

sealed class HouseholdEvent {
    data class AddMember(val userId: String, val role: String) : HouseholdEvent()
    data class RemoveMember(val userId: String) : HouseholdEvent()
    data class UpdateMemberRole(val userId: String, val role: String) : HouseholdEvent()
    data class UpdateHouseholdName(val name: String) : HouseholdEvent()
    object DeleteHousehold : HouseholdEvent()
}