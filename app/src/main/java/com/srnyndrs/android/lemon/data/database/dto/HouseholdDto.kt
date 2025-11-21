package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HouseholdDto(
    @SerialName("household_id")
    val id: String,
    @SerialName("household_name")
    val name: String,
    @SerialName("members")
    val members: List<MemberDto>
)