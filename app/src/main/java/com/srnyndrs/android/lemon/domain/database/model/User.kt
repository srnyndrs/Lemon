package com.srnyndrs.android.lemon.domain.database.model

data class User(
    val user_id: String,
    val username: String,
    val email: String,
    val household_ids: List<String>
)
