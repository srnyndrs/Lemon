package com.srnyndrs.android.lemon.ui.screen.main.content.home

sealed class HomeEvent {
    class DeleteTransaction(val transactionId: String): HomeEvent()
}