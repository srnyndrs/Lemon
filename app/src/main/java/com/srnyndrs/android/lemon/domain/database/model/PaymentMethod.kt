package com.srnyndrs.android.lemon.domain.database.model

data class PaymentMethod(
    val id: String? = null,
    val name: String,
    val icon: String? = null,
    val color: String? = null,
    val type: String? = null,
    val ownerUserId: String? = null,
    val isActive: Boolean = true,
    val inHousehold: Boolean = true,
    val editable: Boolean = false
)
