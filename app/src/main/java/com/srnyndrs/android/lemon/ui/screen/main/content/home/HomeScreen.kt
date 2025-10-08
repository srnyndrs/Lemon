package com.srnyndrs.android.lemon.ui.screen.main.content.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.components.ActionButton
import com.srnyndrs.android.lemon.ui.components.transaction.Transaction
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionRow
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionType
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.Plus

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }

    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(scrollState)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pager
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(256.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp)),
                state = pagerState,
                pageSpacing = 12.dp,
            ) { pageIndex ->
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            // TODO
                        }
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
                        verticalArrangement = Arrangement.Center
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
            // Page indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                repeat(pagerState.pageCount) { index ->
                    val color = if (index == pagerState.currentPage) MaterialTheme.colorScheme.onSurface else Color.Gray
                    Box(
                        modifier = Modifier
                            .size(8.dp)
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
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(0.1f))
            //.padding(12.dp),
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 2.dp),
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
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(5) { index ->
                    TransactionRow(
                        modifier = Modifier.fillMaxWidth(),
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
    }
}

@Preview
@Composable
fun HomScreenPreview() {
    LemonTheme {
        Surface {
            HomeScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}