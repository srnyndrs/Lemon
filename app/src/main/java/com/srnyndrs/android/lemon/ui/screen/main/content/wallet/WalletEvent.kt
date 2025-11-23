package com.srnyndrs.android.lemon.ui.screen.main.content.wallet

import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

sealed class WalletEvent {
    data class ChangePaymentMethod(val paymentMethodId: String): WalletEvent()
    data class AddPaymentMethod(val paymentMethod: PaymentMethod): WalletEvent()
    data class UpdatePaymentMethod(val paymentMethod: PaymentMethod): WalletEvent()
}