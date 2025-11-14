package com.srnyndrs.android.lemon.domain.database.model.dto

import com.srnyndrs.android.lemon.domain.database.model.TransactionType

data class TransactionDetailsDto(
    val title: String,
    val type: TransactionType,
    val amount: Double,
    val paymentMethodId: String,
    val date: String? = null, // ISO 8601 format
    val categoryId: String? = null,
    val recurringPaymentId: String? = null,
    val description: String? = null,
)
