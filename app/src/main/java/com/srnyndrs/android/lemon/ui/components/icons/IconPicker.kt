package com.srnyndrs.android.lemon.ui.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.AllIcons
import compose.icons.FeatherIcons

@Composable
fun IconPicker(
    modifier: Modifier = Modifier,
    initialIcon: ImageVector? = null,
    onIconSelected: (ImageVector) -> Unit
) {

    val icons = FeatherIcons.AllIcons

    var selectedIconIndex by remember {
        mutableIntStateOf(
            icons.indexOf(initialIcon).takeIf { it != -1 } ?: 0
        )
    }

    LazyRow(
        modifier = Modifier
            .then(modifier)
            .requiredHeight(48.dp)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(icons) { index, icon ->
            val selected = index == selectedIconIndex
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        // Handle icon selections
                        selectedIconIndex = index
                        onIconSelected(icon)
                    }
                    .let {
                        if(selected) {
                            it.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        } else {
                            it.border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if(selected) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Transparent)
                )
            }
        }
    }
}

@Preview
@Composable
fun IconPickerPreview() {
    LemonTheme {
        Surface {
            IconPicker(
                modifier = Modifier.fillMaxWidth()
            ) {

            }
        }
    }
}