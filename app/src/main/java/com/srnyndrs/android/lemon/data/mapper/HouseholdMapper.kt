package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.HouseholdDto
import com.srnyndrs.android.lemon.data.database.dto.MemberDto
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.domain.database.model.Member
import kotlin.collections.firstOrNull

fun HouseholdDto.toDomain(
    baseUrl: String
): Household {
    return Household(
        id = id,
        name = name,
        members = members.map { it.toDomain(baseUrl) }
    )
}

fun MemberDto.toDomain(
    baseUrl: String
): Member {
    return Member(
        id = id,
        name = name,
        role = role,
        picture = "$baseUrl/storage/v1/object/public/profile_picture/$id/profile.png"
    )
}