package com.srnyndrs.android.lemon.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.core.graphics.toColorInt

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
    drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
}