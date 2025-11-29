package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyIncome(
    @SerialName("household_id")
    val householdId: String,
    val year: Int,
    val month: Int,
    @SerialName("total_income")
    val totalIncome: Double
)
