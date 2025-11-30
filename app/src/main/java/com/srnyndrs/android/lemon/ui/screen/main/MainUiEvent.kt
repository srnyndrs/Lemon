package com.srnyndrs.android.lemon.ui.screen.main

sealed class MainUiEvent {
    data object ShowBottomSheet: MainUiEvent()
    data object ShowTransactions: MainUiEvent()
    data class ShowTransactionEditor(val transactionId: String? = null): MainUiEvent()
    data object ShowHousehold: MainUiEvent()
    data object NavigateBack: MainUiEvent()
}
