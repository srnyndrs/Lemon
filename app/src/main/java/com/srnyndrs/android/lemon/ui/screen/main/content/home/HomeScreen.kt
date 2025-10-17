package com.srnyndrs.android.lemon.ui.screen.main.content.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.ui.components.ActionButton
import com.srnyndrs.android.lemon.ui.components.transaction.Transaction
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionRow
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionType
import com.srnyndrs.android.lemon.ui.screen.main.MainEvent
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.Plus

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    households: List<Household>,
    selectedHouseholdId: String,
    onEvent: (MainEvent<*>) -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isInitialized by rememberSaveable { mutableStateOf(false) }
    val pagerState = rememberPagerState(
        // TODO: proper way
        initialPage = households.indexOf(households.find { it.id == selectedHouseholdId } ?: households.firstOrNull()),
        initialPageOffsetFraction = 0f
    ) {
        //3
        households.size
    }

    LaunchedEffect(pagerState.currentPage) {
        if (isInitialized) {
            val selectedHousehold = households.getOrNull(pagerState.currentPage)
            selectedHousehold?.let {
                onEvent(MainEvent.SwitchHousehold(it.id))
                Toast.makeText(context, "Switched to ${it.name}", Toast.LENGTH_SHORT).show()
            }
        } else {
            isInitialized = true
        }
    }

    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Monthly Overview",
                style = MaterialTheme.typography.headlineSmall
            )
        }*/

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(256.dp),
            contentAlignment = Alignment.Center
        ) {
            val backgroundColor = MaterialTheme.colorScheme.surface
            // Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(0.2f),
                                MaterialTheme.colorScheme.primaryContainer.copy(0.2f)
                            )
                        )
                    ).drawWithContent {
                        drawContent()
                        drawRect(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Yellow,
                                    Color.Yellow.copy(0.7f),
                                    Color.Yellow.copy(0.4f),
                                    Color.Yellow.copy(0.2f),
                                    backgroundColor
                                ),
                                startY = 0f,
                                endY = size.height - 200f
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
            }
            //
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                    //.background(Color.Black.copy(0.1f)),
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
                    val household = households[pageIndex]
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
                                text = household.name,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if(pagerState.pageCount <= 1) return@Row
                    repeat(pagerState.pageCount) { index ->
                        val color = if (index == pagerState.currentPage) MaterialTheme.colorScheme.onSurface else Color.Gray
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        if (index != pagerState.pageCount - 1) {
                            Spacer(
                                modifier = Modifier
                                    .requiredWidth(4.dp)
                            )
                        }
                    }
                }
            }
        }


        // Actions
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
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
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
                modifier = Modifier.fillMaxSize(),
                households = listOf(
                    Household(
                        id = "1",
                        name = "Private household"
                    ),
                    Household(
                        id = "2",
                        name = "Work"
                    )
                ),
                selectedHouseholdId = "1"
            ) {}
        }
    }
}