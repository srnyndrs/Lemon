package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String,
    val household_id: String,
    val name: String,
    val icon: String,
    val color: String,
    @SerialName("created_at")
    val createdAt: String,
)
