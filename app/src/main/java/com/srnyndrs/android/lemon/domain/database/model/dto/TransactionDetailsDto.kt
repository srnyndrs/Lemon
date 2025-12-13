package com.srnyndrs.android.lemon.domain.database.model.dto

import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDetailsDto(
    val title: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: Double = 0.0,
    @SerialName("payment_method_id")
    val paymentMethodId: String? = null,
    val date: String? = null, // ISO 8601 format
    @SerialName("category_id")
    val categoryId: String? = null,
    @SerialName("recurring_payment_id")
    val recurringPaymentId: String? = null,
    val description: String? = null,
)
