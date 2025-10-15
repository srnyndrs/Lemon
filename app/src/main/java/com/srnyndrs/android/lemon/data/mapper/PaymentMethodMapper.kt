package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

fun PaymentMethodDto.toDomain(): PaymentMethod{
    return PaymentMethod(
        id = payment_method_id,
        name = name,
        color = color ?: "#FFFFFF",
    )
}