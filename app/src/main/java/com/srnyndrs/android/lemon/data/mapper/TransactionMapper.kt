package com.srnyndrs.android.lemon.data.mapper

import com.srnyndrs.android.lemon.data.database.dto.TransactionDto
import com.srnyndrs.android.lemon.data.database.dto.TransactionStatsDto
import com.srnyndrs.android.lemon.data.database.dto.TransactionsView
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto

fun TransactionDetailsDto.toDto(
    householdId: String,
    userId: String
): TransactionDto {
    return TransactionDto(
        householdId = householdId,
        userId = userId,
        paymentMethodId = paymentMethodId,
        type = type.name.lowercase(),
        title = title,
        amount = amount,
        date = date,
        categoryId = categoryId,
        recurringPaymentId = recurringPaymentId,
        description = description
    )
}

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id ?: "",
        type = TransactionType.valueOf(type),
        title = title,
        amount = amount,
        date = date!!, // TODO
        description = description
    )
}

fun TransactionsView.toDomain(): TransactionItem {
    return TransactionItem(
        id = transactionId,
        type = TransactionType.valueOf(type.uppercase()),
        title = title,
        amount = amount,
        categoryName = categoryName,
        color = categoryColor,
        icon = categoryIcon,
        date = transactionDate
    )
}

fun TransactionStatsDto.toDomain(): StatisticGroupItem {
    return StatisticGroupItem(
        categoryName = categoryName,
        icon = icon,
        color = color,
        totalAmount = totalAmount,
        year = year,
        month = month
    )
}
