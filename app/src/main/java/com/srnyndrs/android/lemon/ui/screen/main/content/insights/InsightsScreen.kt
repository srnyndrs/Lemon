package com.srnyndrs.android.lemon.ui.screen.main.content.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.ui.screen.main.components.ColumnChartDiagram
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.formatAsCurrency
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.ChevronRight
import kotlinx.coroutines.launch

@Composable
fun InsightsScreen(
    modifier: Modifier = Modifier,
    statistics: List<StatisticGroupItem>,
    allExpenses: List<Pair<Int, Double>>,
) {

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    // Create list of pairs: month (String) -> total expenses (Double)
    val monthlyExpenses: List<Pair<String, Double>> = allExpenses
        .map { (month, totalAmount) ->
            val monthString = month.let { months[it - 1] }
            val totalExpenses = totalAmount
            Pair(monthString, totalExpenses)
        }

    val pages = listOf(
        "By Category",
        "By Month"
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { 2 }

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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                when (page) {
                    0 -> {
                        PieChartDiagram(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(200.dp),
                            chartData = statistics,
                        )
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(statistics) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = it.categoryName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = it.totalAmount.formatAsCurrency().plus(" Ft"),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        ColumnChartDiagram(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(256.dp),
                            data = monthlyExpenses
                        )
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(monthlyExpenses) { (month, totalAmount) ->
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

@Preview
@Composable
private fun InsightsScreenPreview() {
    LemonTheme {
        Surface {
            InsightsScreen(
                modifier = Modifier.fillMaxSize(),
                statistics = listOf(
                    StatisticGroupItem(
                        categoryName = "Food",
                        color = "#FF0000",
                        totalAmount = 250.0
                    )
                ),
                allExpenses = listOf(
                    Pair(1, 200.0),
                    Pair(2, 450.0),
                    Pair(3, 300.0),
                    Pair(4, 600.0),
                    Pair(5, 150.0),
                    Pair(6, 500.0),
                )
            )
        }
    }
}