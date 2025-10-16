package com.srnyndrs.android.lemon.domain.database.model

data class Category(
    val id: String? = null,
    val householdId: String? = null,
    val name: String,
    val icon: String,
    val color: String
)
