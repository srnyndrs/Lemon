package com.srnyndrs.android.lemon.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun Color.Companion.fromHex(colorString: String): Color {
    return if (colorString.startsWith("#")) {
        Color((colorString).toColorInt())
    } else {
        Color(("#$colorString").toColorInt())
    }
}