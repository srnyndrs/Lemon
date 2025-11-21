package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.HouseholdDto
import com.srnyndrs.android.lemon.data.database.dto.MemberDto
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.domain.database.model.Member

fun HouseholdDto.toDomain(): Household {
    return Household(
        id = id,
        name = name,
        members = members.map { it.toDomain() }
    )
}

fun MemberDto.toDomain(): Member {
    return Member(
        id = id,
        name = name,
        role = role
    )
}