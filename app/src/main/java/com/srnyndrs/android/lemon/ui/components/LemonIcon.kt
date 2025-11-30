package com.srnyndrs.android.lemon.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.srnyndrs.android.lemon.ui.utils.LemonIcons

@Composable
fun LemonIcon(
    modifier: Modifier = Modifier,
    icon: LemonIcons,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    contentDescription: String? = null
) {
    Icon(
        modifier = Modifier.then(modifier),
        painter = painterResource(icon.resource),
        tint = tint,
        contentDescription = contentDescription,
    )
}