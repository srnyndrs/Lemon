package com.srnyndrs.android.lemon.ui.screen.main

import com.srnyndrs.android.lemon.domain.database.model.UserMainData

data class MainState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserMainData = UserMainData(),
    val selectedHouseholdId: String = "",
)
