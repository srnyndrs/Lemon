package com.srnyndrs.android.lemon.ui.screen.main.content.wallet

import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.ui.utils.UiState

data class WalletState(
    val paymentMethods: UiState<List<PaymentMethod>> = UiState.Empty(),
    val transactions: UiState<Map<String, List<TransactionItem>>> = UiState.Empty()
)