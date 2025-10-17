package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodDto(
    @SerialName("household_id")
    val householdId: String,
    @SerialName("payment_method_id")
    val paymentMethodId: String? = null,
    val name: String,
    val icon: String? = null,
    val color: String? = null,
    val type: String? = null,
    @SerialName("owner_user_id")
    val ownerUserId: String
)
