package com.srnyndrs.android.lemon.ui.screen.main

import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod

sealed class MainEvent<T>(val data: T? = null) {
    object Logout: MainEvent<Unit>()
    class AddPaymentMethod(paymentMethod: PaymentMethod): MainEvent<PaymentMethod>(data = paymentMethod)
    class AddCategory(category: Category): MainEvent<Category>(data = category)
    class SwitchHousehold(householdId: String): MainEvent<String>(data = householdId)
}