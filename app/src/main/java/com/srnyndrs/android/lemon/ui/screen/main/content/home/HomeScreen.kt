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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.ui.components.ActionButton
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionList
import com.srnyndrs.android.lemon.ui.screen.main.MainEvent
import com.srnyndrs.android.lemon.ui.screen.main.MainUiEvent
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.formatAsCurrency
import com.srnyndrs.android.lemon.ui.utils.shimmer
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.Home
import compose.icons.feathericons.Plus
import kotlin.compareTo

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    households: List<Household>,
    selectedHouseholdId: String,
    transactions: Map<String, List<TransactionItem>>,
    expenses: Map<TransactionType, Double>,
    isLoading: Boolean,
    onUiEvent: (MainUiEvent) -> Unit,
    onEvent: (MainEvent<*>) -> Unit
) {

    var isInitialized by rememberSaveable { mutableStateOf(false) }
    val initialPage = households.indexOfFirst { it.id == selectedHouseholdId }
    val pagerState = rememberPagerState(
        initialPage = if (initialPage >= 0) initialPage else 0,
        initialPageOffsetFraction = 0f
    ) {
        households.size.takeIf { it > 0 } ?: 1
    }

    val expense = expenses[TransactionType.EXPENSE] ?: 0.0
    val income = expenses[TransactionType.INCOME] ?: 0.0
    val total = expense + income
    val expenseRatio = if (total > 0.0) (expense / total).toFloat() else 0f

    LaunchedEffect(pagerState.currentPage) {
        if (isInitialized) {
            val selectedHousehold = households.getOrNull(pagerState.currentPage)
            selectedHousehold?.let {
                onEvent(MainEvent.SwitchHousehold(it.id))
            }
        } else if (!isLoading){
            isInitialized = true
        }
    }

    Column(
        modifier = Modifier
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(256.dp),
            contentAlignment = Alignment.Center
        ) {

            val backgroundColor = MaterialTheme.colorScheme.surface
            val foregroundColor = MaterialTheme.colorScheme.tertiary

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
                                    foregroundColor,
                                    foregroundColor.copy(0.7f),
                                    foregroundColor.copy(0.4f),
                                    foregroundColor.copy(0.2f),
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Pager
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(256.dp),
                    state = pagerState,
                    pageSpacing = 12.dp,
                ) { pageIndex ->
                        Row (
                            modifier = Modifier
                                .fillMaxSize()
                                .let {
                                    if (isLoading) {
                                        it.shimmer()
                                    } else {
                                        it
                                    }
                                }
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if(!isLoading) {
                                households.getOrNull(pageIndex)?.let { household ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                // TODO: Open household details
                                            }
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(22.dp),
                                                imageVector = FeatherIcons.Home,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = household.name,
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                        Spacer(
                                            modifier = Modifier.requiredHeight(16.dp)
                                        )
                                        Text(
                                            text = (income.minus(expense)).formatAsCurrency() + " Ft", // TODO
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontSize = 28.sp
                                        )
                                        Spacer(
                                            modifier = Modifier.requiredHeight(8.dp)
                                        )
                                        LinearProgressIndicator(
                                            modifier = Modifier
                                                .fillMaxWidth(0.75f)
                                                .padding(vertical = 6.dp),
                                            progress = { expenseRatio },
                                            color = Color.Red,
                                            trackColor = Color.Green,
                                            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                                            gapSize = 0.dp,
                                            drawStopIndicator = {}
                                        )
                                    }
                                }
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
                    if(!isLoading) {
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
                                Spacer(modifier = Modifier.requiredWidth(4.dp))
                            }
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
                onClick = {
                    onUiEvent(MainUiEvent.ShowBottomSheet)
                }
            )
            ActionButton(
                title = "Scan Receipt",
                icon = FeatherIcons.Camera,
                onClick = {
                    // TODO: Open camera screen
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))            ,
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
                        onUiEvent(MainUiEvent.ShowTransactions)
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
                    .fillMaxSize()
                    .padding(bottom = 12.dp, start = 6.dp, end = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TransactionList(
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = isLoading,
                    transactions = transactions,
                    onDelete = {
                        onEvent(MainEvent.DeleteTransaction(it))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
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
                selectedHouseholdId = "1",
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
                expenses = mapOf(
                    TransactionType.EXPENSE to 5000.0,
                    TransactionType.INCOME  to 10000.0
                ),
                isLoading = true,
                onUiEvent = {}
            ) {}
        }
    }
}