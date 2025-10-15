package com.srnyndrs.android.lemon.ui.screen.main

sealed class MainEvent<T>(val data: T? = null) {
    object Logout: MainEvent<Unit>()
    class FetchCategories(val householdId: String): MainEvent<Unit>()

    //class Test(data2: Unit): MainEvent<Unit>(data = data2)

}