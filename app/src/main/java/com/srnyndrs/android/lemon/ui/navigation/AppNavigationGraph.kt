package com.srnyndrs.android.lemon.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.srnyndrs.android.lemon.domain.authentication.model.AuthStatus
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationScreen
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationViewModel
import com.srnyndrs.android.lemon.ui.screen.main.MainScreen
import com.srnyndrs.android.lemon.ui.screen.main.MainViewModel

@Composable
fun AppNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authStatus: AuthStatus,
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
                modifier = Modifier.fillMaxSize().padding(top = 24.dp),
                authenticationState = authenticationState,
                onEvent = { event ->
                    authenticationViewModel.onEvent(event)
                }
            )
        }
        composable(
            route = "main"
        ) {

            val userId = when(authStatus) {
                is AuthStatus.Authenticated -> authStatus.userSession.user?.id ?: ""
                else -> ""
            }

            val mainViewModel = hiltViewModel<MainViewModel, MainViewModel.MainViewModelFactory>(
                creationCallback = { factory -> factory.create(userId = userId) }
            )

            val mainState by mainViewModel.mainState.collectAsState()

            MainScreen(
                modifier = Modifier.fillMaxSize(),
                mainState = mainState,
                onMainEvent = { event ->
                    mainViewModel.onEvent(event)
                }
            )
        }
    }
}