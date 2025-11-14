package com.srnyndrs.android.lemon.ui.screen.main.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.StatisticGroupItem
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun PieChartDiagram(
    modifier: Modifier = Modifier,
    chartData: List<StatisticGroupItem>,
    onClick: () -> Unit = {}
) {

    var data by remember {
        mutableStateOf(
            chartData.map { item -> 
                Pie(
                    label = item.categoryName,
                    data = item.totalAmount,
                    color = Color.fromHex(item.color ?:"#CCCCCC"), // TODO
                    //selectedColor = Color.Green
                )
            }.let {
                // Default pie when there's no data
                it.ifEmpty {
                    listOf(
                        Pie(
                            label = "Empty",
                            data = 1.0,
                            color = Color.LightGray,
                        )
                    )
                }
            }
        )
    }

    PieChart(
        modifier = Modifier.then(modifier),
        data = data,
        onPieClick = {
            onClick()
            /*println("${it.label} Clicked")
            val pieIndex = data.indexOf(it)
            data = data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }*/
        },
        selectedScale = 1.2f,
        scaleAnimEnterSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        scaleAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300),
        spaceDegree = 0f,
        selectedPaddingDegree = 12f,
        style = Pie.Style.Stroke(width = 32.dp)
    )
}

@Preview
@Composable
fun PieChartDiagramPreview() {
    LemonTheme {
        Surface {
            PieChartDiagram(
                modifier = Modifier.size(200.dp),
                chartData = listOf(
                    StatisticGroupItem("Food", "", "#FFB74D", 200.0),
                    StatisticGroupItem("Transport", "", "#64B5F6", 150.0),
                    StatisticGroupItem("Entertainment", "", "#BA68C8", 100.0),
                )
            )
        }
    }
}