package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

fun PaymentMethodDto.toDomain(): PaymentMethod{
    return PaymentMethod(
        id = paymentMethodId,
        name = name,
        color = color ?: "#FFFFFF",
    )
}

fun PaymentMethod.toDto(
    householdId: String,
    userId: String
): PaymentMethodDto {
    return PaymentMethodDto(
        paymentMethodId = null,
        householdId = householdId,
        name = name,
        color = color,
        type = "card", // TODO: deprecate
        ownerUserId = userId
    )
}