package com.srnyndrs.android.lemon.ui.screen.main

import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto

sealed class MainEvent<T>(val data: T? = null) {
    object Logout: MainEvent<Unit>()
    class AddPaymentMethod(paymentMethod: PaymentMethod): MainEvent<PaymentMethod>(data = paymentMethod)
    class AddCategory(category: Category): MainEvent<Category>(data = category)
    class AddTransaction(transactionDetailsDto: TransactionDetailsDto): MainEvent<TransactionDetailsDto>(data = transactionDetailsDto)
    class SwitchHousehold(householdId: String): MainEvent<String>(data = householdId)
    class DeleteTransaction(transactionId: String): MainEvent<String>(data = transactionId)
}