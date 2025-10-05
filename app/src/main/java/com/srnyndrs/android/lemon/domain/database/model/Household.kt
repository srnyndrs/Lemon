package com.srnyndrs.android.lemon.domain.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Household(
    val id: String,
    val name: String
)
