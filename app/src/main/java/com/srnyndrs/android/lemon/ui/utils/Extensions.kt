package com.srnyndrs.android.lemon.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun Color.Companion.fromHex(colorString: String) = Color(("#$colorString").toColorInt())