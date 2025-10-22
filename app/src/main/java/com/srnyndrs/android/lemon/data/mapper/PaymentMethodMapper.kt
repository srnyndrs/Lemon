package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.PaymentMethodDto
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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

fun PaymentMethodDto.toJsonObject(
    householdId: String,
    userId: String
): JsonObject {
    return JsonObject(
        mapOf(
            "p_color" to (this.color?.let { JsonPrimitive(it) } ?: JsonNull),
            "p_household_id" to JsonPrimitive(householdId),
            "p_icon" to (this.icon?.let { JsonPrimitive(it) } ?: JsonNull),
            "p_name" to JsonPrimitive(this.name),
            "p_type" to JsonPrimitive("card"), // TODO: deprecate
            "p_user_id" to JsonPrimitive(userId),
        )
    )
}