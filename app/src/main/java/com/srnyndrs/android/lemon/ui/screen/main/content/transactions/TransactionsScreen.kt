package com.srnyndrs.android.lemon.ui.screen.main.content.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.srnyndrs.android.lemon.ui.components.transaction.Transaction
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionList
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionRow
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionType
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Airplay
import compose.icons.feathericons.Plus

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Occurring Payments",
            style = MaterialTheme.typography.titleLarge
        )
        // Occurring payments
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Add new occurring payment
                Card(
                    modifier = Modifier
                        .requiredWidth(64.dp)
                        .requiredHeight(72.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        // TODO
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.4f)
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primaryContainer.copy(0.5f),
                                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f),
                                )
                            ))
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = FeatherIcons.Plus,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.requiredHeight(4.dp))
                        Text(
                            text = "Add payment",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 8.sp
                        )
                    }
                }
            }
            items(3) {
                Card(
                    modifier = Modifier
                        .requiredWidth(64.dp)
                        .requiredHeight(72.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        // TODO
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(
                                colors = listOf(
                                    Color.Green,
                                    Color.Green.copy(0.5f),
                                    Color.Green.copy(0.3f),
                                )
                            ))
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer.copy(0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // TODO: Picture
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp),
                                imageVector = FeatherIcons.Airplay,
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.requiredHeight(4.dp))
                        Text(
                            text = "$it. payment",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        }
        Text(
            text = "Transactions",
            style = MaterialTheme.typography.titleLarge
        )
        TransactionList(
            modifier = Modifier.fillMaxWidth(),
            transactions = mapOf(
                "June 20, 2024" to listOf(
                    Transaction(
                        id = "1",
                        name = "Grocery Store",
                        amount = 54000,
                        transactionType = TransactionType.EXPENSE,
                    ),
                    Transaction(
                        id = "2",
                        name = "Salary",
                        amount = 150000,
                        transactionType = TransactionType.INCOME,
                    )
                ),
                "June 19, 2024" to listOf(
                    Transaction(
                        id = "3",
                        name = "Electricity Bill",
                        amount = 75000,
                        transactionType = TransactionType.EXPENSE,
                    )
                )
            )
        )
    }
}

@Preview
@Composable
fun TransactionsScreenPreview() {
    LemonTheme {
        Surface {
            TransactionsScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            )
        }
    }
}