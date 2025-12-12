package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.UserWithHousehold
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.domain.database.model.UserMainData

fun List<UserWithHousehold>.toDomain(
    baseUrl: String
): UserMainData {
    val households = this.map {
        Household(
            id = it.householdId,
            name = it.householdName,
        )
    }

    return UserMainData(
        userId = this.firstOrNull()?.userId.orEmpty(),
        username = this.firstOrNull()?.username.orEmpty(),
        email = this.firstOrNull()?.email.orEmpty(),
        profilePictureUrl = this.firstOrNull()?.userId.let { id ->
            "$baseUrl/storage/v1/object/public/profile_picture/$id/profile.png"
        },
        households = households,
    )
}