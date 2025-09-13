package com.srnyndrs.android.lemon.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationScreen
import com.srnyndrs.android.lemon.ui.screen.authentication.AuthenticationViewModel

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
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val email = when(sessionStatus) {
                    is SessionStatus.Authenticated -> sessionStatus.userSession.user?.email
                    else -> null
                }
                Text(
                    modifier = Modifier,
                    text = "Welcome ${email}!"
                )
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        onLogout()
                        navController.navigate("auth") {
                            popUpTo("main") {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Log Out"
                    )
                }
            }
        }
    }
}