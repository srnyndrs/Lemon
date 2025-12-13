package com.srnyndrs.android.lemon.ui.screen.scan

import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto

sealed interface SplitBillUiState {
    data object Idle : SplitBillUiState
    data object Loading : SplitBillUiState
    data object Saving : SplitBillUiState
    data object Saved : SplitBillUiState
    data class Success(val transactionDetails: TransactionDetailsDto) : SplitBillUiState
    data class Error(val message: String) : SplitBillUiState
}