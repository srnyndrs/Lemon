package com.srnyndrs.android.lemon.ui.screen.main

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.Menu
import compose.icons.feathericons.Package
import compose.icons.feathericons.Pocket
import compose.icons.feathericons.User

sealed class Screens(val route: String, val title: String, val icon: ImageVector? = null) {
    object Home : Screens("home", "Home", FeatherIcons.Home)
    object Wallet: Screens("wallet", "Wallet", FeatherIcons.Pocket)
    object Categories: Screens("categories", "Categories", FeatherIcons.Package)
    object Profile: Screens("profile", "Profile", FeatherIcons.User)
}
