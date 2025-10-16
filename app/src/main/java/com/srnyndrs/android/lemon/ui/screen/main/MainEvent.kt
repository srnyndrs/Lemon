package com.srnyndrs.android.lemon.ui.screen.main

import com.srnyndrs.android.lemon.domain.database.model.Category

sealed class MainEvent<T>(val data: T? = null) {
    object Logout: MainEvent<Unit>()
    class FetchCategories(val householdId: String): MainEvent<Unit>()
    class AddCategory(category: Category): MainEvent<Category>(data = category)

    //class Test(data2: Unit): MainEvent<Unit>(data = data2)

}