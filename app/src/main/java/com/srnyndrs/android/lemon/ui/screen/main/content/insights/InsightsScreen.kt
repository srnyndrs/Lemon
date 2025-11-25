package com.srnyndrs.android.lemon.ui.screen.main.content.insights

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.ui.components.UiStateContainer
import com.srnyndrs.android.lemon.ui.screen.main.components.ColumnChartDiagram
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import com.srnyndrs.android.lemon.ui.utils.formatAsCurrency
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.ChevronRight
import kotlinx.coroutines.launch

@Composable
fun InsightsScreen(
    modifier: Modifier = Modifier,
    insightsState: InsightsState
) {

    val months = listOf(
        "January" to 0.0,
        "February" to 0.0,
        "March" to 0.0,
        "April" to 0.0,
        "May" to 0.0,
        "June" to 0.0,
        "July" to 0.0,
        "August" to 0.0,
        "September" to 0.0,
        "October" to 0.0,
        "November" to 0.0,
        "December" to 0.0
    )

    val pages = listOf(
        "Category Statistics",
        "Monthly Expenses"
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { 2 }

    var selectedCategoryIndex by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronLeft,
                    contentDescription = null
                )
            }
            Text(
                text = pages[pagerState.currentPage],
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(10)
                    }
                }
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronRight,
                    contentDescription = null
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.then(modifier),
            verticalAlignment = Alignment.Top,
            userScrollEnabled = false,
            state = pagerState
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                when (page) {
                    0 -> {
                        UiStateContainer(
                            modifier = Modifier.fillMaxSize(),
                            state = insightsState.statistics
                        ) { statistics ->
                            PieChartDiagram(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .requiredHeight(200.dp),
                                chartData = statistics,
                                selectedIndex = selectedCategoryIndex,
                            ) { categoryIndex ->
                                selectedCategoryIndex = categoryIndex
                            }
                            HorizontalDivider()
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Categories",
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if(statistics.isEmpty()) {
                                    item {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            text = "No statistics available.",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                                itemsIndexed(statistics) { index, item ->
                                    val isSelected = selectedCategoryIndex == index
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedCategoryIndex = if(isSelected) {
                                                    null
                                                } else {
                                                    index
                                                }
                                            }
                                            .let {
                                                if (isSelected) {
                                                    it.background(
                                                        MaterialTheme.colorScheme.primary.copy(0.1f),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                } else {
                                                    it
                                                }
                                            }
                                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(0.05f), RectangleShape)
                                            .padding(horizontal = 6.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .background(Color.fromHex(item.color ?: "#000000"))
                                            )
                                            Text(
                                                text = item.categoryName,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                        Text(
                                            text = item.totalAmount.formatAsCurrency().plus(" Ft"),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        UiStateContainer(
                            modifier = Modifier.fillMaxSize(),
                            state = insightsState.allExpenses
                        ) { allExpenses ->

                            val monthlyExpenses = months.mapIndexed { index, (monthName, _) ->
                                val totalAmount = allExpenses.find { (month, _) -> month == index + 1 }?.second ?: 0.0
                                Pair(monthName, totalAmount)
                            }

                            ColumnChartDiagram(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(256.dp),
                                data = monthlyExpenses
                            )
                            HorizontalDivider()
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Months",
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(monthlyExpenses.reversed().filter { it.second != 0.0 }) { (month, totalAmount) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = month,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = totalAmount.formatAsCurrency().plus(" Ft"),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun InsightsScreenPreview() {
    LemonTheme {
        Surface {
            InsightsScreen(
                modifier = Modifier.fillMaxSize(),
                insightsState = InsightsState(
                    statistics = UiState.Success(
                        listOf(
                        StatisticGroupItem( "Food", "Expenses on food", "#FFB74D", 200.0),
                        StatisticGroupItem( "Transport", "Expenses on transport", "#4DB6AC", 450.0),
                        StatisticGroupItem( "Entertainment", "Expenses on entertainment", "#BA68C8", 300.0),
                        StatisticGroupItem( "Shopping", "Expenses on shopping", "#E57373", 600.0),
                        StatisticGroupItem( "Health", "Expenses on health", "#81C784", 150.0),
                        StatisticGroupItem( "Bills", "Expenses on bills", "#64B5F6", 500.0),
                    )),
                    allExpenses = UiState.Success(
                        listOf(
                        Pair(1, 200.0),
                        Pair(2, 450.0),
                        Pair(3, 300.0),
                        Pair(4, 600.0),
                        Pair(5, 150.0),
                        Pair(6, 500.0),
                    )
                    )
                )
            )
        }
    }
}