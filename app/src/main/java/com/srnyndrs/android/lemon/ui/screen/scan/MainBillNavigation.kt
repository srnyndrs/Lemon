package com.srnyndrs.android.lemon.ui.screen.scan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.ui.screen.scan.content.SplitBillScreen
import com.srnyndrs.android.lemon.ui.screen.scan.content.landing.SplitBillLandingScreen
import com.srnyndrs.android.lemon.ui.screen.scan.content.result.BillResultScreen
import com.srnyndrs.android.lemon.ui.screen.scan.content.scanner.SplitBillMainScreen

sealed class Screen(val route: String) {
    object SplitBillLandingScreen : Screen("split_bill_landing")
    object SplitBillMainScreen : Screen("split_bill_main")
    object BillResultScreen : Screen("bill_result")
    object SplitBillScreen : Screen("split_bill")
}

@Composable
fun MainBillNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val billViewModel: SplitBillViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SplitBillLandingScreen.route
    ) {
        composable(Screen.SplitBillLandingScreen.route) {
            SplitBillLandingScreen(
                modifier = modifier,
                navController = navController
            )
        }
        composable(Screen.SplitBillMainScreen.route) {
            SplitBillMainScreen(
                modifier = modifier,
                navController = navController,
                viewModel = billViewModel
            )
        }
        composable(Screen.BillResultScreen.route) {
            BillResultScreen(
                modifier = modifier,
                navController = navController,
                viewModel = billViewModel
            )
        }
        composable(Screen.SplitBillScreen.route) {
            SplitBillScreen(
                modifier = modifier,
                navController = navController,
                viewModel = billViewModel
            )
        }
    }
}