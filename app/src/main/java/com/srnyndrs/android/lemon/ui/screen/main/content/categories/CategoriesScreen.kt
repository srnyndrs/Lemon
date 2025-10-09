package com.srnyndrs.android.lemon.ui.screen.main.content.categories

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bold
import compose.icons.feathericons.Plus

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categories: List<Category>
) {

    Column(
        modifier = Modifier
            .then(modifier)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(56.dp)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                onClick = {
                    // TODO
                }
            ) {
                Icon(
                    imageVector = FeatherIcons.Plus,
                    contentDescription = "Add Category" // TODO
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(categories) { index, category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(72.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Companion.fromHex(category.color),
                        contentColor = Color.Black
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    onClick = {
                        // TODO: Handle category click
                    }
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Bold,
                                contentDescription = null,
                            )
                        }
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoriesScreenPreview() {
    LemonTheme {
        Surface {
            CategoriesScreen(
                modifier = Modifier.fillMaxSize(),
                categories = listOf(
                    Category(
                        id = "1",
                        name = "Food",
                        color = "FFE57373",
                        icon = "Bold"
                    ),
                    Category(
                        id = "2",
                        name = "Transport",
                        color = "FF64B5F6",
                        icon = "Bold"
                    ),
                    Category(
                        id = "3",
                        name = "Shopping",
                        color = "FFFFB74D",
                        icon = "Bold"
                    )
                )
            )
        }
    }
}