package com.srnyndrs.android.lemon.ui.components.selectors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.ui.components.items.CategoryItem
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.Check
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp

@Composable
fun CategorySelector(
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
    categories: List<Category>,
    onSelect: (Int?) -> Unit
) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val uncategorizedCategory = Category(
        id = "",
        name = "Uncategorized",
        color = "#9E9E9E",
        icon = "DEFAULT"
    )

    Column(
        modifier = Modifier.then(modifier)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(5.dp)),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(5.dp))
        ) {
            CategoryItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(64.dp),
                category = selectedIndex?.let { categories[it] } ?: uncategorizedCategory,
                trailingContent = {
                    Icon(
                        imageVector = if(isExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                        contentDescription = null
                    )
                }
            ) {
                isExpanded = !isExpanded
            }
        }
        AnimatedVisibility(isExpanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(256.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                item {
                    CategoryItem(
                        modifier = Modifier.fillMaxWidth(),
                        category = uncategorizedCategory,
                        trailingContent = {
                            if(selectedIndex == null) {
                                Icon(
                                    imageVector = FeatherIcons.Check,
                                    contentDescription = null
                                )
                            }
                        }
                    ) {
                        onSelect(null)
                        isExpanded = false
                    }
                }
                itemsIndexed(categories) { index, category ->
                    CategoryItem(
                        modifier = Modifier.fillMaxWidth(),
                        category = category,
                        trailingContent = {
                            if(index == selectedIndex) {
                                Icon(
                                    imageVector = FeatherIcons.Check,
                                    contentDescription = null
                                )
                            }
                        }
                    ) {
                        onSelect(index)
                        isExpanded = false
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CategorySelectorPreview() {
    LemonTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            var selectedIndex by rememberSaveable { mutableStateOf<Int?>(null) }
            CategorySelector(
                modifier = Modifier.fillMaxWidth(),
                selectedIndex = selectedIndex,
                categories = listOf(
                    Category(
                        id = "1",
                        name = "Groceries",
                        color = "#FF5733",
                        icon = "DEFAULT"
                    ),
                    Category(
                        id = "2",
                        name = "Utilities",
                        color = "#33FF57",
                        icon = "DEFAULT"
                    ),
                    Category(
                        id = "3",
                        name = "Entertainment",
                        color = "#3357FF",
                        icon = "DEFAULT"
                    )
                )
            ) {
                selectedIndex = it
            }
        }
    }
}