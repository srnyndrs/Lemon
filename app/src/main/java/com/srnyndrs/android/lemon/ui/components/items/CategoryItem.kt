package com.srnyndrs.android.lemon.ui.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.ui.components.LemonIcon
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.LemonIcons
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.Check
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    trailingContent: @Composable (() -> Unit),
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.fromHex(category.color))
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                LemonIcon(
                    modifier = Modifier.size(28.dp).padding(3.dp),
                    icon = LemonIcons.valueOf(category.icon),
                    contentDescription = category.name,
                    tint = Color.Black
                )
            }
            // Category Name
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        trailingContent()
    }
}

@PreviewLightDark
@Composable
private fun CategoryItemPreview() {
    LemonTheme {
        Surface {
            CategoryItem(
                modifier = Modifier.fillMaxWidth(),
                category = Category(
                    id = "1",
                    name = "Shopping",
                    color = "FF5733",
                    icon = "CART"
                ),
                trailingContent = {},
                onClick = {}
            )
        }
    }
}