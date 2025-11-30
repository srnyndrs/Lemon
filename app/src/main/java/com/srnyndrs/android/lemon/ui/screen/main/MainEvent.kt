package com.srnyndrs.android.lemon.ui.screen.main

import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.ui.screen.main.content.household.HouseholdEvent

sealed class MainEvent {
    object Logout: MainEvent()
    class SwitchHousehold(val householdId: String): MainEvent()
    class CreateHousehold(val householdName: String): MainEvent()
    data class UpdateHouseholdName(val householdId: String, val name: String) : MainEvent()
    data class DeleteHousehold(val householdId: String): MainEvent()
}