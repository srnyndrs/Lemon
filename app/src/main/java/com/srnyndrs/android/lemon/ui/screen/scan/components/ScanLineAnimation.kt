package com.srnyndrs.android.lemon.ui.screen.scan.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ScanLineAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_line_animation")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing), // 2 seconds for one full scan
            repeatMode = RepeatMode.Reverse
        ), label = "offset_y"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp) // Define the visible scan area
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val lineHeight = 4.dp.toPx()
            val currentY = size.height * offsetY

            drawLine(
                color = Color.Green,
                start = Offset(0f, currentY),
                end = Offset(size.width, currentY),
                strokeWidth = lineHeight
            )

            drawRect(
                color = Color.White.copy(alpha = 0.3f), // Light border
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}