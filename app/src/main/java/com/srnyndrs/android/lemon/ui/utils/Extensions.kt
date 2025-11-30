package com.srnyndrs.android.lemon.ui.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.core.graphics.toColorInt
import androidx.compose.animation.core.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun Modifier.shimmerEffect(isLoading: Boolean): Modifier {
    return if (isLoading) {
        this.shimmer()
    } else {
        this
    }
}

fun Modifier.shimmer(
    shimmerColors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    ),
    durationMillis: Int = 1000
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )

    drawWithContent {
        drawContent()
        val width = size.width
        val height = size.height
        val gradientWidth = 0.2f * width
        val x = translateAnim.value % (width + gradientWidth)
        drawRect(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x - gradientWidth, 0f),
                end = Offset(x, height)
            ),
            size = size
        )
    }
}

fun Color.Companion.fromHex(colorString: String): Color {
    return if (colorString.startsWith("#")) {
        Color((colorString).toColorInt())
    } else {
        Color(("#$colorString").toColorInt())
    }
}

fun DrawScope.drawSpline(points: List<Offset>, color: Color) {
    if (points.size < 2) return
    val path = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size - 1) {
            val prev = points[i - 1]
            val curr = points[i]
            val next = points[i + 1]
            // Simple quadratic spline between points
            quadraticTo(
                curr.x, curr.y,
                (curr.x + next.x) / 2, (curr.y + next.y) / 2
            )
        }
        // Line to last point
        lineTo(points.last().x, points.last().y)
    }
    drawPath(path, color, style = Stroke(width = 4f))
}

@SuppressLint("DefaultLocale")
fun Double.formatAsCurrency(): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }
    val df = DecimalFormat("#,##0", symbols).apply {
        isGroupingUsed = true
        maximumFractionDigits = 0
    }
    return df.format(this.toLong())
}

fun String.toMillis(): Long? {
    return try {
        val systemZone = ZoneId.systemDefault()
        val instant = if (this.length <= 10) {
            val localDate = LocalDate.parse(this)
            localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        } else {
            val localDateTime = LocalDateTime.parse(this)
            localDateTime.atZone(systemZone).toInstant()
        }
        instant.toEpochMilli()
    } catch (_: Exception) {
        null
    }
}
