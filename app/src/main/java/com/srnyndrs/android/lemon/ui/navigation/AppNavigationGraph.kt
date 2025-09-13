package com.srnyndrs.android.lemon.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationScreen
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationViewModel

@Composable
fun AppNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.then(modifier),
        navController = navController,
        startDestination = "auth"
    ) {
        composable(
            route = "auth"
        ) {
            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
            val authenticationState by authenticationViewModel.authState.collectAsState()
            AuthenticationScreen(
                modifier = Modifier.fillMaxSize(),
                authenticationState = authenticationState,
                onEvent = { event ->
                    authenticationViewModel.onEvent(event)
                }
            )
        }
        composable(
            route = "main"
        ) {
            Text(
                modifier = Modifier,
                text = "Welcome!"
            )
        }
    }
}