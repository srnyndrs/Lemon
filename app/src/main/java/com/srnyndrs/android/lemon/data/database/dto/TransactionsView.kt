package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsView(
    @SerialName("transaction_id")
    val transactionId: String,
    @SerialName("household_id")
    val householdId: String,
    val year: Int,
    val month: Int,
    @SerialName("category_name")
    val categoryName: String? = null,
    @SerialName("category_icon")
    val categoryIcon: String? = null,
    @SerialName("category_color")
    val categoryColor: String? = null,
    @SerialName("payment_method_id")
    val paymentMethodId: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("amount")
    val amount: Double,
    @SerialName("transaction_date")
    val transactionDate: String
)
