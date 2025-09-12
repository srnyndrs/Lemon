package com.srnyndrs.android.lemon.ui.screen.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.ui.utils.UiState

@Composable
fun AuthenticationScreen(
    modifier: Modifier = Modifier,
    authenticationState: UiState<Unit>,
    onEvent: (AuthenticationEvent) -> Unit,
    onSuccess: () -> Unit
) {

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }

    LaunchedEffect(authenticationState) {
        if(authenticationState is UiState.Success) {
            onSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.then(modifier),
        topBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
            ) {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("login")
                    },
                    alwaysShowLabel = true,
                    label = {
                        Text(text = "Login")
                    },
                    icon = { /* TODO: Add icon */ }
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("register")
                    },
                    alwaysShowLabel = true,
                    label = {
                        Text(text = "Register")
                    },
                    icon = { /* TODO: Add icon */ }
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
                .padding(innerPadding),
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(text = "Login Screen")
                    Button(
                        onClick = {
                            onEvent(AuthenticationEvent.OnLoginClick("user", "password"))
                        }
                    ) {
                        Text(text = "Login")
                    }
                }
            }
            composable("register") {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(text = "Register Screen")
                    Button(
                        onClick = {
                            onEvent(AuthenticationEvent.OnRegisterClick("user", "password", "email"))
                        }
                    ) {
                        Text(text = "Register")
                    }
                }
            }
        }
    }
}