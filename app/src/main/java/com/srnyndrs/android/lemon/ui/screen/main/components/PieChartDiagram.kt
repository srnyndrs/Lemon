package com.srnyndrs.android.lemon.ui.screen.main.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    data: List<StatisticGroupItem>,
    selectedIndex: Int? = null,
    onClick: (Int?) -> Unit
) {

    var pies by remember {
        mutableStateOf(
            data.mapIndexed { index, item ->
                Pie(
                    selected = selectedIndex == index,
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

    var selectedPercent by remember { mutableStateOf<Float?>(null) }

    val selectPie = { index: Int? ->
        pies = pies.mapIndexed { mapIndex, pie ->
            pie.copy(selected = index == mapIndex)
        }
        selectedPercent = if (index != null) {
            val selectedData = pies[index].data.toFloat()
            val totalData = pies.sumOf { it.data }.toFloat()
            if (totalData == 0f) 0f else (selectedData / totalData * 100f)
        } else {
            null
        }
    }

    LaunchedEffect(selectedIndex) {
        selectPie(selectedIndex)
    }

    Box(
        modifier = Modifier.then(modifier)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick(null)
                selectPie(null)
            },
        contentAlignment = Alignment.Center
    ) {
        PieChart(
            modifier = Modifier.size(200.dp),
            data = pies,
            onPieClick = {
                if(it.data == 0.0) return@PieChart
                val pieIndex = pies.indexOf(it)
                onClick(pieIndex)
                selectPie(pieIndex)
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
        Text(
            text = selectedPercent?.let {
                "%.0f".format(selectedPercent).plus(" %")
            } ?: "",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
fun PieChartDiagramPreview() {
    LemonTheme {
        Surface {
            PieChartDiagram(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(200.dp),
                selectedIndex = 0,
                data = listOf(
                    StatisticGroupItem("Food", "", "#FFB74D", 200.0),
                    StatisticGroupItem("Transport", "", "#64B5F6", 150.0),
                    StatisticGroupItem("Entertainment", "", "#BA68C8", 100.0),
                )
            ) {}
        }
    }
}