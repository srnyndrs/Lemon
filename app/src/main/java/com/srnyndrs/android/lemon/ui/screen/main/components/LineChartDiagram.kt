package com.srnyndrs.android.lemon.ui.screen.main.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.formatAsCurrency
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun LineChartDiagram(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Double>>,
) {

    val lineColor = MaterialTheme.colorScheme.onSurface
    val dotColor = MaterialTheme.colorScheme.surface

    val lines = remember {
         listOf(
             Line(
                label = "Incomes",
                values = data.map { it.second },
                color = SolidColor(Color(0xFF23af92)),
                firstGradientFillColor = Color(0xFF23af92).copy(alpha = .5f),
                secondGradientFillColor = Color.Transparent,
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
                curvedEdges = true,
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(dotColor),
                    strokeWidth = 1.dp,
                    radius = 3.dp,
                    strokeColor = SolidColor(Color(0xFF23af92)),
                )
            )
         )
    }

    Column(
        modifier = Modifier.then(modifier)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            data = lines,
            animationMode = AnimationMode.Together(),
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

        )
    }


}

@Preview
@Composable
fun LineChartDiagramPreview() {

    val sampleData = listOf(
        "Jan" to 500.0,
        "Feb" to 750.0,
        "Mar" to 300.0,
        "Apr" to 900.0,
        "May" to 650.0,
        "Jun" to 800.0,
    )

    LemonTheme {
        Surface {
            LineChartDiagram(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(256.dp)
                    .padding(12.dp),
                data = sampleData
            )
        }
    }
}