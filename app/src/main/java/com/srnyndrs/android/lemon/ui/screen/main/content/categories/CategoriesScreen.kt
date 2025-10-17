package com.srnyndrs.android.lemon.ui.screen.main.content.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.ui.components.forms.CategoryForm
import com.srnyndrs.android.lemon.ui.screen.main.MainEvent
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bold
import compose.icons.feathericons.Menu
import compose.icons.feathericons.Plus

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    payments: List<PaymentMethod>,
    onAddCategory: (Category) -> Unit
) {

    val pagerState = rememberPagerState(initialPage = 1) { payments.size + 1 }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .then(modifier)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Payment Methods",
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            // TODO: List all payment methods but disable which not part of the household yet
            // Pager
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(256.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp)),
                state = pagerState,
                pageSpacing = 12.dp,
            ) { pageIndex ->
                val payment = payments.getOrNull(pageIndex - 1) ?: PaymentMethod(
                    id = "123",
                    name = "placeholder",
                    color = "FF64B5F6",
                )
                val color = Color.Companion.fromHex(payment.color)
                // First Index
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(180.dp)
                        //.shadow(1.dp, RoundedCornerShape(8.dp), ambientColor = MaterialTheme.colorScheme.onSurface)
                        .padding(1.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                            listOf(
                                    color,
                                    color.copy(0.7f),
                                    color.copy(0.3f),
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if(pageIndex == 0) {
                        TextButton(
                            onClick = {
                                // TODO: Add Payment Method
                                showDialog = true
                            }
                        ) {
                            Text(
                                text = "Add Payment Method",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent)
                                .padding(top = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(24.dp)
                                    .background(Color.Black)
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth())
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(0.5f)
                                    .padding(start = 6.dp, end = 6.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = payment.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(0.5f)
                                    .padding(horizontal = 22.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = FeatherIcons.Menu,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }
            // Page indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                repeat(pagerState.pageCount) { index ->
                    val color = if (index == pagerState.currentPage) MaterialTheme.colorScheme.onSurface else Color.Gray
                    val first = index == 0
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .let {
                                if (!first) it.border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    CircleShape
                                ) else it.border(
                                    1.dp,
                                    color,
                                    CircleShape
                                )
                            }
                            .background(color)
                            ,
                        contentAlignment = Alignment.Center
                    ) {
                        if(first) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = FeatherIcons.Plus,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    }
                }
            }
        }

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
                    showDialog = true
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
        // Dialog
        if(showDialog) {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
                ) {
                    CategoryForm(
                        modifier = Modifier.fillMaxWidth(),
                        onConfirm = {
                            onAddCategory(it)
                            showDialog = false
                        }
                    ) {
                        showDialog = false
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
                        icon = "Bold",
                        householdId = "1"
                    ),
                    Category(
                        id = "2",
                        name = "Transport",
                        color = "FF64B5F6",
                        icon = "Bold",
                        householdId = "1"
                    ),
                    Category(
                        id = "3",
                        name = "Shopping",
                        color = "FFFFB74D",
                        icon = "Bold",
                        householdId = "1"
                    )
                ),
                payments = listOf(
                    PaymentMethod(
                        id = "1",
                        name = "Credit Card",
                        color = "FF64B5F6",
                    ),
                    PaymentMethod(
                        id = "2",
                        name = "Debit Card",
                        color = "FFFFB74D",
                    ),
                    PaymentMethod(
                        id = "3",
                        name = "Cash",
                        color = "FF81C784",
                    )
                )
            ) {}
        }
    }
}