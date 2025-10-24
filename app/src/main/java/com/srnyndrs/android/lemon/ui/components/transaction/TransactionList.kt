package com.srnyndrs.android.lemon.ui.components.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    transactions: Map<String, List<Transaction>> = emptyMap()
) {

    LazyColumn(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        transactions.forEach { (date, transactionList) ->
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
            }
            items(transactionList.size) { index ->
                TransactionRow(
                    modifier = Modifier.fillMaxWidth(),
                    transaction = transactionList[index]
                ) {
                    // TODO: Handle transaction click
                }
            }
        }
    }

}

@Preview
@Composable
fun TransactionListPreview() {
    LemonTheme {
        Surface {
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(6.dp),
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
                            amount = 7500,
                            transactionType = TransactionType.EXPENSE,
                        )
                    )
                )
            )
        }
    }
}