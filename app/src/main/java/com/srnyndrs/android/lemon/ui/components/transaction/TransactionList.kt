package com.srnyndrs.android.lemon.ui.components.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.shimmer

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: Map<String, List<TransactionItem>>? = null,
    onDelete: (String) -> Unit,
    onClick: (String) -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if(isLoading) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "2025-01-01",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
            }
            items(3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp,MaterialTheme.colorScheme.onSurface.copy(0.05f), RoundedCornerShape(8.dp))
                        .shimmer(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Spacer(modifier = Modifier.fillMaxWidth())
                }
            }
        } else {

            if(transactions.isNullOrEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "No transactions available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                        )
                    }
                }
            }

            transactions?.forEach { (date, transactionList) ->
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
                    val item = transactionList[index]
                    TransactionRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(96.dp),
                        transaction = item,
                        onDelete = {
                            onDelete(item.id)
                        }
                    ) {
                        onClick(item.id)
                    }
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
                isLoading = true,
                transactions = mapOf(
                    "June 20, 2024" to listOf(
                        TransactionItem(
                            id = "1",
                            title = "Grocery Store",
                            amount = 54000.0,
                            type = TransactionType.EXPENSE,
                            date = "June 19, 2024"
                        ),
                        TransactionItem(
                            id = "2",
                            title = "Salary",
                            amount = 150000.0,
                            type = TransactionType.INCOME,
                            date = "June 19, 2024"
                        )
                    ),
                    "June 19, 2024" to listOf(
                        TransactionItem(
                            id = "3",
                            title = "Electricity Bill",
                            amount = 7500.0,
                            type = TransactionType.EXPENSE,
                            date = "June 19, 2024"
                        )
                    )
                ),
                onDelete = {},
            ) {

            }
        }
    }
}