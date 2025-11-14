package com.srnyndrs.android.lemon.domain.database.model

data class StatisticGroupItem(
    val categoryName: String,
    val icon: String? = null,
    val color: String? = null,
    val totalAmount: Double,
)