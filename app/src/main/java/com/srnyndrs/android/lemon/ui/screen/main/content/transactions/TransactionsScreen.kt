package com.srnyndrs.android.lemon.ui.screen.main.content.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.ui.components.UiStateContainer
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionList
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Airplay
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.ChevronRight
import compose.icons.feathericons.Plus

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    transactionState: TransactionState,
    onEvent: (TransactionsEvent) -> Unit
) {

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    
    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            text = "Transactions",
            style = MaterialTheme.typography.headlineMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    var month = transactionState.selectedMonth - 1
                    var year = transactionState.selectedYear
                    if (month < 1) {
                        month = 12
                        year -= 1
                    }
                    onEvent(
                        TransactionsEvent.ChangeDate(
                            year = year,
                            month = month
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronLeft,
                    contentDescription = null
                )
            }
            Text(
                text = transactionState.let {
                    "${it.selectedYear}  ${months[it.selectedMonth - 1]}"
                }
            )
            IconButton(
                onClick = {
                    var month = transactionState.selectedMonth + 1
                    var year = transactionState.selectedYear
                    if (month > 12) {
                        month = 1
                        year += 1
                    }
                    onEvent(
                        TransactionsEvent.ChangeDate(
                            year = year,
                            month = month
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronRight,
                    contentDescription = null
                )
            }
        }

        UiStateContainer(
            modifier = Modifier.fillMaxWidth(),
            state = transactionState.transactions
        ) { isLoading, transactions ->
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                isLoading = isLoading,
                transactions = transactions,
                onDelete = { /* TODO */ },
            ) {}
        }

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
                    .padding(6.dp),
                transactionState = TransactionState(
                    transactions = UiState.Success(
                        data = mapOf(
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
                        )
                    )
                )
            ){}
        }
    }
}