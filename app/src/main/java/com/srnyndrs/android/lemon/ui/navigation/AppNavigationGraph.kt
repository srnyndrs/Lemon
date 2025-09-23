package com.srnyndrs.android.lemon.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationScreen
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationViewModel
import com.srnyndrs.android.lemon.ui.screen.main.MainScreen
import com.srnyndrs.android.lemon.ui.screen.main.MainViewModel

@Composable
fun AppNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sessionStatus: SessionStatus,
    onLogout: () -> Unit,
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

            val userId = when(sessionStatus) {
                is SessionStatus.Authenticated -> sessionStatus.userSession.user?.id ?: ""
                else -> ""
            }

            val mainViewModel = hiltViewModel<MainViewModel, MainViewModel.MainViewModelFactory>(
                creationCallback = { factory -> factory.create(userId = userId) }
            )

            val email = when(sessionStatus) {
                is SessionStatus.Authenticated -> sessionStatus.userSession.user?.email
                else -> null
            }

            val user by mainViewModel.user.collectAsState()

            MainScreen(
                modifier = Modifier.fillMaxSize(),
                user = user,
                email = email,
                onLogout = {
                    onLogout()
                    navController.navigate("auth") {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}