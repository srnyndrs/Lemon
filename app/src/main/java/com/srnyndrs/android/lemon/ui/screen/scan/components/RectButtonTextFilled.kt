package com.srnyndrs.android.lemon.ui.screen.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RectButtonTextFilled(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    colorButton: Color,
    colorLabel: Color,
    padding: Dp,
    height: Dp
) {
    TextButton(
        onClick = onClick,
        modifier
            .fillMaxWidth()
            .padding(start = padding, end = padding)
            .clip(RoundedCornerShape(4.dp))
            .padding()
            .height(height)
            .background(color = colorButton)
    ) {
        Text(
            text = label, style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorLabel
            )
        )
    }
}