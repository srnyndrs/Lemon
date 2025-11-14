package com.srnyndrs.android.lemon.data.database

enum class DatabaseEndpoint(val path: String) {
    USER_HOUSEHOLDS_VIEW("user_households"),
    CATEGORIES_TABLE("categories"),
    PAYMENT_METHODS_VIEW("household_payment_methods_view"),
    ADD_PAYMENT_FUNCTION("add_payment_method"),
    TRANSACTIONS_VIEW("household_transactions_view")
}