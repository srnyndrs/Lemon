package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWithHousehold(
    val user_id: String,
    val email: String,
    val username: String,
    val household_id: String,
    val household_name: String,
    val household_role: String,
    val household_created_at: String
)
