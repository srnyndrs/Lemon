package com.srnyndrs.android.lemon.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.authentication.model.SessionStatus
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    email: String?,
    sessionStatus: SessionStatus,
    onLogout: () -> Unit
) {
    Scaffold(
        modifier = Modifier.then(modifier),
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = "Welcome ${email}!"
            )
            PieChartDiagram()
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onLogout()
                }
            ) {
                Text(
                    text = "Log Out"
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun MainScreenPreview() {
    LemonTheme {
        Surface {
            MainScreen(
                modifier = Modifier.fillMaxSize(),
                email = "test@test.com",
                sessionStatus = SessionStatus.Unauthenticated,
                onLogout = {}
            )
        }
    }
}