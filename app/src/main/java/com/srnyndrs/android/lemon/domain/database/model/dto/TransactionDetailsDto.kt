package com.srnyndrs.android.lemon.domain.database.model.dto

import com.srnyndrs.android.lemon.domain.database.model.TransactionType

data class TransactionDetailsDto(
    val title: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: Double = 0.0,
    val paymentMethodId: String? = null,
    val date: String? = null, // ISO 8601 format
    val categoryId: String? = null,
    val recurringPaymentId: String? = null,
    val description: String? = null,
)
