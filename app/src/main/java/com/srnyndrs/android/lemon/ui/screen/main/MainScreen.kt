package com.srnyndrs.android.lemon.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.User
import com.srnyndrs.android.lemon.ui.components.ActionButton
import com.srnyndrs.android.lemon.ui.components.transaction.Transaction
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionRow
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionType
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.Code
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.Plus
import compose.icons.feathericons.User

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    user: UiState<User>,
    email: String?,
    onLogout: () -> Unit
) {

    var privacyMode by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.then(modifier),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                // Username
                Text(text = "Lemon")
                /*Row(
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
                }*/
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        privacyMode = !privacyMode
                        // TODO: authentication
                    }
                ) {
                    Icon(
                        imageVector = if(privacyMode) FeatherIcons.EyeOff else FeatherIcons.Eye,
                        contentDescription = null
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Monthly Overview",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            /*Text(
                modifier = Modifier,
                text = "Welcome ${email}!"
            )*/

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(256.dp)
                        .padding(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                    )
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PieChartDiagram(
                            modifier = Modifier.size(128.dp)
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Private household",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(
                                modifier = Modifier.requiredHeight(12.dp)
                            )
                            Text(
                                text = "1200 Ft",
                                style = MaterialTheme.typography.titleLarge
                            )
                            LinearProgressIndicator(
                                progress = {
                                    // TODO: calculate from transactions
                                    0.6f
                                },
                                modifier = Modifier
                                        .fillMaxWidth(0.75f)
                                        .padding(vertical = 6.dp),
                                color = Color.Red,
                                trackColor = Color.Green,
                                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                                gapSize = 0.dp,
                                drawStopIndicator = {}
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    repeat(3) { index ->
                        val color = if (index > 0) MaterialTheme.colorScheme.onSurface else Color.Gray
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    title = "Add new",
                    icon = FeatherIcons.Plus,
                    onClick = {}
                )
                ActionButton(
                    title = "Scan Receipt",
                    icon = FeatherIcons.Camera,
                    onClick = {}
                )
                /*TextButton(
                    onClick = {},
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = FeatherIcons.Plus,
                        contentDescription = null
                    )
                    Text(text = "Add transaction")
                }
                TextButton(
                    onClick = {},
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = FeatherIcons.Plus,
                        contentDescription = null
                    )
                    Text(text = "Scan from receipt")
                }*/
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    TextButton(
                        onClick = {
                            // TODO: Navigate to all transactions
                        }
                    ) {
                        Text(
                            text = "See all",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(4) { index ->
                        TransactionRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            transaction = Transaction(
                                id = "$index",
                                name = "Transaction ${index + 1}",
                                transactionType = if(index % 2 == 0) TransactionType.EXPENSE else TransactionType.INCOME,
                                amount = (index + 1) * 2530
                            )
                        ) { }
                    }
                }
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