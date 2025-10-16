package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.CategoryDto
import com.srnyndrs.android.lemon.domain.database.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id ?: "",
        name = name,
        icon = icon,
        color = color,
        householdId = household_id
    )
}

fun Category.toDto(
    householdId: String
): CategoryDto {
    return CategoryDto(
        id = null,
        household_id = householdId,
        name = name,
        icon = icon,
        color = color,
        createdAt = null,
    )
}