package com.srnyndrs.android.lemon.domain.genai
import kotlinx.serialization.Serializable

@Serializable
data class BillItem(
    val name: String = "",
    val unitPrice: String = "0.00",
    val quantity: String = "1",
    val totalPrice: String = "0.00"
)