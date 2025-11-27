package com.srnyndrs.android.lemon.ui.screen.main.content.home

sealed class HomeEvent {
    class SwitchHousehold(val householdId: String): HomeEvent()
    class DeleteTransaction(val transactionId: String): HomeEvent()
}