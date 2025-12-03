package com.srnyndrs.android.lemon.ui.screen.main.content.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.ui.components.LemonIcon
import com.srnyndrs.android.lemon.ui.components.UiStateContainer
import com.srnyndrs.android.lemon.ui.components.forms.CategoryForm
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.LemonIcons
import com.srnyndrs.android.lemon.ui.utils.UiState
import com.srnyndrs.android.lemon.ui.utils.fromHex
import com.srnyndrs.android.lemon.ui.utils.shimmer
import com.srnyndrs.android.lemon.ui.utils.shimmerEffect
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bold
import compose.icons.feathericons.Plus
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    categoriesState: CategoryState,
    onEvent: (CategoryEvent) -> Unit
) {

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf<CategoryUiEvent?>(null) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(56.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
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
                    showDialog = CategoryUiEvent.ADD
                }
            ) {
                Icon(
                    imageVector = FeatherIcons.Plus,
                    contentDescription = "Add Category"
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(categoriesState.actionStatus is UiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
        UiStateContainer(
            modifier = Modifier.fillMaxSize(),
            state = categoriesState.categories
        ) { isLoading, categories ->
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 8.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if(isLoading) {
                    items(6) {
                        Card(
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .fillMaxWidth()
                                .requiredHeight(72.dp)
                                .shimmerEffect(isLoading),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            ),
                        ) {}
                    }
                } else {
                    categories?.let {
                        itemsIndexed(categories) { index, category ->

                            var showPopup by remember { mutableStateOf(false) }

                            Card(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .fillMaxWidth()
                                    .requiredHeight(72.dp)
                                    .combinedClickable(
                                        onClick = {},
                                        onLongClick = {
                                            showPopup = true
                                        }
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Companion.fromHex(category.color),
                                    contentColor = Color.Black
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
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
                                        LemonIcon(
                                            modifier = Modifier.size(28.dp).padding(3.dp),
                                            icon = LemonIcons.valueOf(category.icon),
                                            tint = Color.Black
                                        )
                                    }
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                DropdownMenu(
                                    modifier = Modifier.align(Alignment.End),
                                    expanded = showPopup,
                                    onDismissRequest = {
                                        showPopup = false
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = "Edit")
                                        },
                                        onClick = {
                                            showPopup = false
                                            selectedCategory = category
                                            showDialog = CategoryUiEvent.UPDATE
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = "Delete")
                                        },
                                        onClick = {
                                            showPopup = false
                                            selectedCategory = category
                                            showDialog = CategoryUiEvent.DELETE
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        showDialog?.let { uiEvent ->
            Dialog(
                onDismissRequest = {
                    showDialog = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
                ) {
                    when(uiEvent) {
                        CategoryUiEvent.ADD -> {
                            CategoryForm(
                                modifier = Modifier.fillMaxWidth(),
                                onConfirm = { category ->
                                    onEvent(CategoryEvent.AddCategory(category))
                                }
                            ) {
                                showDialog = null
                            }
                        }
                        CategoryUiEvent.UPDATE -> {
                            CategoryForm(
                                modifier = Modifier.fillMaxWidth(),
                                category = selectedCategory,
                                onConfirm = { category ->
                                    onEvent(CategoryEvent.UpdateCategory(category))
                                }
                            ) {
                                showDialog = null
                                selectedCategory = null
                            }
                        }
                        CategoryUiEvent.DELETE -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    text = "Are you sure you want to delete this category?",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .requiredHeight(42.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
                                ) {
                                    TextButton(
                                        modifier = Modifier.weight(0.5f),
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        ),
                                        onClick = {
                                            showDialog = null
                                            selectedCategory = null
                                        }
                                    ) {
                                        Text(
                                            text = "No"
                                        )
                                    }
                                    VerticalDivider(
                                        color = MaterialTheme.colorScheme.surface,
                                        thickness = 1.dp
                                    )
                                    TextButton(
                                        modifier = Modifier.weight(0.5f),
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.primary
                                        ),
                                        onClick = {
                                            onEvent(CategoryEvent.DeleteCategory(selectedCategory?.id!!))
                                            showDialog = null
                                            selectedCategory = null
                                        }
                                    ) {
                                        Text(
                                            text = "Yes"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class CategoryUiEvent {
    ADD,
    UPDATE,
    DELETE,
}

@Preview
@Composable
fun CategoryScreenPreview() {
    LemonTheme {
        Surface {
            CategoryScreen(
                modifier = Modifier.fillMaxSize(),
                categoriesState = CategoryState(
                    categories = UiState.Success(
                        data = listOf(
                            Category(
                                id = "1",
                                name = "Food",
                                color = "FFE57373",
                                icon = "DINING",
                                householdId = "1"
                            ),
                            Category(
                                id = "2",
                                name = "Transport",
                                color = "FF64B5F6",
                                icon = "AIRPLANE",
                                householdId = "1"
                            ),
                            Category(
                                id = "3",
                                name = "Shopping",
                                color = "FFFFB74D",
                                icon = "CART",
                                householdId = "1"
                            )
                        )
                    )
                )
            ) {}
        }
    }
}

@Preview
@Composable
fun CategoryScreenLoadingPreview(modifier: Modifier = Modifier) {
    LemonTheme {
        Surface {
            CategoryScreen(
                modifier = Modifier.fillMaxWidth(),
                categoriesState = CategoryState(
                    categories = UiState.Loading()
                )
            ) { }
        }
    }
}