package com.srnyndrs.android.lemon.ui.screen.main.content.transactions.editor

import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.ui.utils.UiState

data class TransactionEditorState(
    val transaction: UiState<TransactionDetailsDto> = UiState.Empty(),
    val categories: UiState<List<Category>> = UiState.Empty(),
    val paymentMethods: UiState<List<PaymentMethod>> = UiState.Empty()
)
