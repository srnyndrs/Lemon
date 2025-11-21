package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodDto(
    @SerialName("payment_method_id")
    val id: String,
    val name: String,
    val icon: String?,
    val color: String?,
    val type: String?,
    @SerialName("owner_user_id")
    val ownerUserId: String,
    @SerialName("is_active")
    val isActive: Boolean
)
