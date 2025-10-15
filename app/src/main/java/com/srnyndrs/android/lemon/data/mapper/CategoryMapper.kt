package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.CategoryDto
import com.srnyndrs.android.lemon.domain.database.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        color = color,
    )
}