package com.srnyndrs.android.lemon.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.User
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Code
import compose.icons.feathericons.User

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    user: UiState<User>,
    email: String?,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        .clickable {
                            // TODO: Open profile settings
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder for profile image
                    Icon(
                        imageVector = FeatherIcons.User,
                        contentDescription = null,
                    )
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(0.2f), RoundedCornerShape(8.dp))
                        .clickable {
                            // TODO: Expand selector
                        }
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Private Household",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .rotate(90f),
                        imageVector = FeatherIcons.Code,
                        contentDescription = null,
                    )
                }
            }

            when(user) {
                is UiState.Loading -> {
                    Text(
                        modifier = Modifier,
                        text = "Loading user data..."
                    )
                }
                is UiState.Success -> {
                    Text(
                        modifier = Modifier,
                        text = "Hello ${user.data.username}"
                    )
                }
                is UiState.Error -> {
                    Text(
                        modifier = Modifier,
                        text = user.message
                    )
                }
                is UiState.Empty -> {}
            }

            Text(
                modifier = Modifier,
                text = "Welcome ${email}!"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(256.dp)
                    .padding(6.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PieChartDiagram()
                    Text("1200 Ft")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

            }

            /*Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onLogout()
                }
            ) {
                Text(
                    text = "Log Out"
                )
            }*/
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    LemonTheme {
        Surface {
            MainScreen(
                modifier = Modifier.fillMaxSize(),
                user = UiState.Empty(),
                email = "test@test.com",
                onLogout = {}
            )
        }
    }
}