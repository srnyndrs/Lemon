package com.srnyndrs.android.lemon.ui.screen.main

sealed class MainUiEvent {
    data object ShowBottomSheet: MainUiEvent()
    data object ShowTransactions: MainUiEvent()
}
