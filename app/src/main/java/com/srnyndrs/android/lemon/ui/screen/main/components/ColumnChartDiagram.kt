package com.srnyndrs.android.lemon.ui.screen.main.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.formatAsCurrency
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun ColumnChartDiagram(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Double>>,
) {

    val bars = remember {
        data.map { (label, value) ->
            Bars(
                label = label,
                values = listOf(
                    Bars.Data(
                        value = value,
                        color = Brush.linearGradient(
                            listOf(Color.Blue, Color.Cyan)
                        )
                    )
                ),
            )
        }
    }

    val lineColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier.then(modifier)
    ) {
        ColumnChart(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            data = bars,
            /*maxValue = 100.0,
            minValue = 0.0,*/
            gridProperties = GridProperties(
                enabled = false
            ),
            labelHelperProperties = LabelHelperProperties(
                enabled = false
            ),
            labelProperties = LabelProperties(
                enabled = true,
                padding = 12.dp,
                textStyle = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                labels = buildList {
                    repeat(12) {
                        add((it + 1).toString())
                    }
                },
                rotation = LabelProperties.Rotation(
                    mode = LabelProperties.Rotation.Mode.Force,
                    degree = 0f,
                    padding = 0.dp
                )
            ),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                count = IndicatorCount.CountBased(count = 3),
                contentBuilder = {
                    // TODO: maximize label characters
                    it.formatAsCurrency().plus(" Ft")
                },
                textStyle = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                padding = 4.dp
            ),
            popupProperties = PopupProperties(
                enabled = true,
                contentBuilder = {
                    it.formatAsCurrency().plus(" Ft")
                },
                containerColor = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.surface
                )
            ),
            dividerProperties = DividerProperties(
                enabled = true,
                yAxisProperties = LineProperties(
                    color = Brush.verticalGradient(
                        listOf(
                            lineColor.copy(0.25f),
                            lineColor.copy(0.25f)
                        )
                    ),
                    thickness = 3.dp
                ),
                xAxisProperties = LineProperties(
                    color = Brush.verticalGradient(
                        listOf(
                            lineColor.copy(0.25f),
                            lineColor.copy(0.25f)
                        )
                    ),
                    thickness = 3.dp
                )
            ),
            barProperties = BarProperties(
                cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                spacing = 6.dp,
                thickness = 16.dp
            ),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
        )
    }
}

@Preview
@Composable
private fun ColumChartDiagramPreview() {
    LemonTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ColumnChartDiagram(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(356.dp),
                data = listOf(
                    "Jan" to 120.0,
                    "Feb" to 80.0,
                    "Mar" to 150.0,
                    "Apr" to 90.0,
                    "May" to 200.0,
                )
            )
        }
    }
}