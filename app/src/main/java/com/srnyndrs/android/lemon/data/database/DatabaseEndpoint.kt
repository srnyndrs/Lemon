package com.srnyndrs.android.lemon.data.database

enum class DatabaseEndpoint(val path: String) {
    USER_HOUSEHOLDS("user_with_households"),
    CATEGORIES("categories"),
    PAYMENT_METHODS("household_payment_methods_view"),
    PAYMENT_METHODS_ROOT("payment_methods")
}