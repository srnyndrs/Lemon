package com.srnyndrs.android.lemon.data.database

enum class DatabaseEndpoint(val path: String) {
    USER_HOUSEHOLDS_VIEW("user_households"),
    USERS_VIEW("users_view"),
    CATEGORIES_TABLE("categories"),
    PAYMENT_METHODS_VIEW("household_payment_methods_view"),
    ADD_PAYMENT_FUNCTION("add_payment_method"),
    DEACTIVATE_PAYMENT_METHOD_FUNCTION("deactivate_payment_method"),
    LINK_PAYMENT_FUNCTION("link_payment_method_to_household"),
    UNLINK_PAYMENT_FUNCTION("unlink_payment_method_from_household"),
    TRANSACTIONS_VIEW("household_transactions_view")
}