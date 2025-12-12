package com.srnyndrs.android.lemon.domain.database.model

import kotlinx.serialization.Serializable

@Serializable
data class UserMainData(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val households: List<Household> = emptyList(),
)