package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWithHousehold(
    @SerialName("user_id")
    val userId: String,
    val email: String,
    val username: String,
    @SerialName("household_id")
    val householdId: String,
    @SerialName("household_name")
    val householdName: String,
    @SerialName("household_role")
    val householdRole: String,
    @SerialName("household_created_at")
    val householdCreatedAt: String
)
