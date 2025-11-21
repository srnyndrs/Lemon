package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
    @SerialName("user_id")
    val id: String,
    @SerialName("username")
    val name: String,
    @SerialName("role")
    val role: String
)