package com.srnyndrs.android.lemon.ui.components.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.LemonColors
import com.srnyndrs.android.lemon.ui.utils.fromHex

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    initialColor: String? = null,
    onSelection: (String) -> Unit
) {

    val colors = LemonColors.entries.map { it.colorHex }

    var selectedColorIndex by remember {
        mutableIntStateOf(
        colors.indexOf(initialColor).takeIf { it != -1 } ?: 0
        )
    }

    LazyRow(
        modifier = Modifier.then(modifier)
            .requiredHeight(48.dp)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(colors) { index, colorHex ->
            val selected = index == selectedColorIndex
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {
                            // Handle color selections
                            selectedColorIndex = index
                            onSelection(colorHex)
                        }
                        .background(Color.fromHex(colorHex))
                        .let {
                            if(selected) {
                                it.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            } else {
                                it.border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            }
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    LemonTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            ColorPicker(
                modifier = Modifier
                    .requiredWidth(256.dp)
                    //.border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
                    .padding(6.dp),
                //initialColor = "#B2EBF2"
            ) {}
        }
    }
}