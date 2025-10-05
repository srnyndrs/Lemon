package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.UserWithHousehold
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.domain.database.model.UserMainData

fun List<UserWithHousehold>.toDomain(): UserMainData {
    val households = this.map {
        Household(
            id = it.household_id,
            name = it.household_name,
        )
    }

    return UserMainData(
        userId = this.firstOrNull()?.user_id.orEmpty(),
        username = this.firstOrNull()?.username.orEmpty(),
        email = this.firstOrNull()?.email.orEmpty(),
        households = households,
    )
}