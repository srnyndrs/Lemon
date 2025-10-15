package com.srnyndrs.android.lemon.data.database.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodDto(
    val household_id: String,
    val payment_method_id: String,
    val name: String,
    val icon: String?,
    val color: String?,
    val type: String,
    val owner_user_id: String
)
