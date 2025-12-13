package com.srnyndrs.android.lemon.ui.screen.scan

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.srnyndrs.android.lemon.ui.screen.main.MainViewModel
import com.srnyndrs.android.lemon.ui.screen.scan.content.SplitBillScreen
import com.srnyndrs.android.lemon.ui.screen.scan.content.landing.SplitBillLandingScreen
import com.srnyndrs.android.lemon.ui.screen.scan.content.result.BillResultScreen
import com.srnyndrs.android.lemon.ui.screen.scan.content.scanner.SplitBillMainScreen



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainBillNavigation(
    modifier: Modifier = Modifier,
    householdId: String,
    userId: String
) {

    val navController = rememberNavController()
    val billViewModel: SplitBillViewModel = hiltViewModel<SplitBillViewModel, SplitBillViewModel.SplitBillViewModelFactory>(
        creationCallback = { factory -> factory.create(
            householdId = householdId,
            userId = userId
        )}
    )

    val uiState by billViewModel.uiState.collectAsState()

    NavHost(
        modifier = Modifier.then(modifier),
        navController = navController,
        startDestination = ScanScreen.SplitBillLandingScanScreen.route
    ) {
        composable(ScanScreen.SplitBillLandingScanScreen.route) {
            SplitBillLandingScreen(
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )
        }
        composable(ScanScreen.SplitBillMainScanScreen.route) {
            SplitBillMainScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                navController = navController,
            ) { event ->
                billViewModel.onEvent(event)
            }
        }
        composable(ScanScreen.BillResultScanScreen.route) {

            val categories by billViewModel.categories.collectAsState()
            val payments by billViewModel.paymentMethods.collectAsState()

            BillResultScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                categories = categories,
                payments = payments,
                onEvent = { event ->
                    billViewModel.onEvent(event)
                }
            ) {
                // On successful save, navigate back to the main split bill screen
                navController.popBackStack(ScanScreen.SplitBillLandingScanScreen.route, inclusive = false)
            }
        }
        composable(ScanScreen.SplitBillScanScreen.route) {
            SplitBillScreen(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                viewModel = billViewModel
            )
        }
    }
}