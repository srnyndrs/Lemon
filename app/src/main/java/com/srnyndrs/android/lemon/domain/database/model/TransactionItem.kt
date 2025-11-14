package com.srnyndrs.android.lemon.domain.database.model

data class TransactionItem(
    val id: String,
    val type: TransactionType,
    val title: String,
    val amount: Double,
    val date: String,
    val categoryName: String? = null,
    val color: String? = null,
    val icon: String? = null
)
