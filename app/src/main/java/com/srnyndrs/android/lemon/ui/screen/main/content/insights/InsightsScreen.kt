package com.srnyndrs.android.lemon.ui.screen.main.content.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.ui.screen.main.components.PieChartDiagram

@Composable
fun InsightsScreen(
    modifier: Modifier = Modifier,
    statistics: List<StatisticGroupItem>,
) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Insights Screen",
            style = MaterialTheme.typography.headlineSmall
        )
        PieChartDiagram(
            modifier = Modifier.size(128.dp),
            chartData = statistics,
        ) {

        }
    }
}