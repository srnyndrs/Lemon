package com.srnyndrs.android.lemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.domain.authentication.model.AuthStatus
import com.srnyndrs.android.lemon.ui.SessionViewModel
import com.srnyndrs.android.lemon.ui.navigation.AppNavigationGraph
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashscreen = installSplashScreen()
        var keepSplashScreen = true

        splashscreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        lifecycleScope.launch {
            delay(5000L)
            keepSplashScreen = false
        }

        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val sessionViewModel = hiltViewModel<SessionViewModel>()
            val sessionStatus by sessionViewModel.authStatus.collectAsState()

            LaunchedEffect(sessionStatus) {
                // Auto-login
                when(sessionStatus) {
                    is AuthStatus.Unauthenticated -> {
                        keepSplashScreen = false
                        navController.navigate("auth") {
                            popUpTo("main") {
                                inclusive = true
                            }
                        }
                    }
                    is AuthStatus.Unknown -> {
                        // Stay on the current screen
                    }
                    is AuthStatus.Authenticated -> {
                        navController.navigate("main") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }

                        // Ensure main screen is ready
                        delay(500L)
                        keepSplashScreen = false
                    }
                }
            }

            LemonTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    AppNavigationGraph(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding()),
                        navController = navController,
                        authStatus = sessionStatus,
                    )
                }
            }
        }
    }
}