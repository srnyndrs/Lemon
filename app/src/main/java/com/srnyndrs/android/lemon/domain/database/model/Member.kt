package com.srnyndrs.android.lemon.domain.database.model

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val id: String,
    val name: String,
    val role: String
)
