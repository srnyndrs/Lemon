package com.srnyndrs.android.lemon.ui.screen.main.content.transactions

sealed class TransactionsEvent {
    data class ChangeDate(val year: Int, val month: Int): TransactionsEvent()
}
