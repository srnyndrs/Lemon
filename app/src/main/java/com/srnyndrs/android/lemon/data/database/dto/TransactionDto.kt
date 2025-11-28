package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: String? = null,
    @SerialName("household_id")
    val householdId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("payment_method_id")
    val paymentMethodId: String? = null,
    val type: String,
    val title: String,
    val amount: Double,
    @SerialName("transaction_date")
    val date: String? = null,
    @SerialName("category_id")
    val categoryId: String? = null,
    @SerialName("recurring_payment_id")
    val recurringPaymentId: String? = null,
    val description: String? = null,
)
