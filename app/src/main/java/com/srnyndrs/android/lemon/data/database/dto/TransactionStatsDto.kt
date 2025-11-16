package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatsDto(
    @SerialName("household_id")
    val householdId: String,
    val year: Int,
    val month: Int,
    @SerialName("category_name")
    val categoryName: String,
    @SerialName("category_icon")
    val icon: String? = null,
    @SerialName("category_color")
    val color: String? = null,
    @SerialName("total_amount")
    val totalAmount: Double,
)
