package com.srnyndrs.android.lemon.ui.screen.main

import androidx.compose.ui.graphics.vector.ImageVector
import com.srnyndrs.android.lemon.R
import com.srnyndrs.android.lemon.ui.utils.LemonIcons
import compose.icons.FeatherIcons
import compose.icons.feathericons.Aperture
import compose.icons.feathericons.Home
import compose.icons.feathericons.PieChart
import compose.icons.feathericons.Pocket
import compose.icons.feathericons.User

sealed class Screens(val route: String, val title: String, val icon: Int? = null) {
    object Home : Screens("home", "Home", R.drawable.home)
    object Insights: Screens("insights", "Insights", R.drawable.chart_pie)
    object Wallet: Screens("wallet", "Wallet", R.drawable.wallet)
    object Categories: Screens("categories", "Categories", R.drawable.tag_multiple)
    object Profile: Screens("profile", "Profile")
    object Transactions: Screens("transactions", "Transactions")
    object Household: Screens("household", "Household")
    object TransactionEditor: Screens("transaction_editor", "Transaction Editor")
}
