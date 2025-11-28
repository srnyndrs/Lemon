package com.srnyndrs.android.lemon.ui.screen.main.content.transactions.editor

import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto

sealed class TransactionEditorEvent {
    data class AddTransaction(val transaction: TransactionDetailsDto): TransactionEditorEvent()
}
