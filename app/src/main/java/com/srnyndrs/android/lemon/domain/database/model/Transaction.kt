package com.srnyndrs.android.lemon.domain.database.model

data class Transaction(
    val id: String,
    val title: String,
    val type: TransactionType,
    val amount: Double,
    val date: String,
    val description: String? = null,
)

enum class TransactionType {
    INCOME, EXPENSE
}