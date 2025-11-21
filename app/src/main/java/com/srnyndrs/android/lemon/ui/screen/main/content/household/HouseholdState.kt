package com.srnyndrs.android.lemon.ui.screen.main.content.household

import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.ui.utils.UiState

data class HouseholdState(
    val household: UiState<Household> = UiState.Empty(),
)