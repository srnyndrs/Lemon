package com.srnyndrs.android.lemon.ui.screen.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.ui.screen.authentication.components.LoginForm
import com.srnyndrs.android.lemon.ui.screen.authentication.components.RegisterForm
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState

@Composable
fun AuthenticationScreen(
    modifier: Modifier = Modifier,
    authenticationState: UiState<Unit>,
    onEvent: (AuthenticationEvent) -> Unit
) {

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.then(modifier),
        topBar = {
            TabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp),
                selectedTabIndex = selectedItem
            ) {
                Tab(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("login")
                    },
                    text = {
                        Text(text = "Login")
                    }
                )
                Tab(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("register")
                    },
                    text = {
                        Text(text = "Register")
                    }
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(48.dp)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when(authenticationState) {
                    is UiState.Loading -> {
                        LinearProgressIndicator()
                    }
                    is UiState.Error -> {
                        Text(text = "Error: ${authenticationState.message}")
                    }
                    else -> {}
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginForm(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    onSubmit = { email, password ->
                        onEvent(AuthenticationEvent.OnLoginClick(email, password))
                    }
                )
            }
            composable("register") {
                RegisterForm(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    onSubmit = { email, password ->
                        onEvent(AuthenticationEvent.OnRegisterClick(email, password))
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AuthenticationScreenPreview() {
    LemonTheme {
        Surface {
            AuthenticationScreen(
                modifier = Modifier.fillMaxSize(),
                authenticationState = UiState.Empty(),
                onEvent = {}
            )
        }
    }
}