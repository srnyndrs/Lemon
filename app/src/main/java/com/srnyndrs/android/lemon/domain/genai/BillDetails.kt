package com.srnyndrs.android.lemon.domain.genai

import kotlinx.serialization.Serializable

@Serializable
data class BillDetails(
    val dateTime: String = "N/A",
    val restaurantName: String = "",
    val items: List<BillItem> = emptyList(),
    val subTotal: String = "0.00",
    val tax: String = "0.00",
    val service: String = "0.00",
    val discount: String = "0.00",
    val others: String = "0.00",
    val total: String = "0.00"
)