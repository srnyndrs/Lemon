package com.srnyndrs.android.lemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus
import com.srnyndrs.android.lemon.ui.SessionViewModel
import com.srnyndrs.android.lemon.ui.navigation.AppNavigationGraph
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val sessionViewModel = hiltViewModel<SessionViewModel>()
            val sessionStatus by sessionViewModel.sessionStatus.collectAsState()

            LaunchedEffect(sessionStatus) {
                // Auto-login
                when(sessionStatus) {
                    is SessionStatus.Unauthenticated -> {
                        navController.navigate("auth") {
                            popUpTo("main") {
                                inclusive = true
                            }
                        }
                    }
                    is SessionStatus.Unknown -> {
                        // Stay on the current screen
                    }
                    is SessionStatus.Authenticated -> {
                        navController.navigate("main") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    }
                }
            }

            LemonTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    /*
                    topBar = {
                        Row(
                            modifier = Modifier.fillMaxWidth().requiredHeight(56.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            when(sessionStatus) {
                                SessionStatus.Authenticated -> {
                                    Row(
                                        modifier = Modifier.fillMaxHeight(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Authenticated")
                                        Button(
                                            onClick = {
                                                sessionViewModel.logout()
                                                navController.navigate("auth") {
                                                    popUpTo("main") {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        ) {
                                            Text(text = "Logout", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                                SessionStatus.Unauthenticated -> {
                                    Text(text = "Not Authenticated")
                                }
                                SessionStatus.Unknown -> {
                                    Text(text = "Unknown")
                                }
                            }
                        }
                    }
                    */
                ) { innerPadding ->
                    AppNavigationGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        sessionStatus = sessionStatus,
                    )
                }
            }
        }
    }
}