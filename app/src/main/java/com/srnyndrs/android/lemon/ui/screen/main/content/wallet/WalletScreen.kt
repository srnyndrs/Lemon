package com.srnyndrs.android.lemon.ui.screen.main.content.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.ui.components.UiStateContainer
import com.srnyndrs.android.lemon.ui.components.forms.PaymentMethodForm
import com.srnyndrs.android.lemon.ui.components.transaction.TransactionList
import com.srnyndrs.android.lemon.ui.screen.main.content.category.CategoryEvent
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.Eye
import compose.icons.feathericons.Menu
import compose.icons.feathericons.Plus

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    state: WalletState,
    onEvent: (WalletEvent) -> Unit
) {

    var showDialog by remember { mutableStateOf<PaymentUiEvent?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Payment Methods
        UiStateContainer(
            modifier = Modifier.fillMaxWidth(),
            state = state.paymentMethods
        ) { isLoading, payments ->

            if (!isLoading) {
                val pagerState =
                    rememberPagerState(initialPage = 1) { (payments?.size?.plus(1)) ?: 2 }

                var showDropdown by remember { mutableStateOf(false) }

                LaunchedEffect(pagerState.currentPage) {
                    if (pagerState.currentPage != 0) {
                        payments?.get(pagerState.currentPage - 1)?.id?.let {
                            onEvent(WalletEvent.ChangePaymentMethod(it))
                        }
                    } else {
                        onEvent(WalletEvent.ClearTransactions)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Payment Methods",
                        style = MaterialTheme.typography.titleLarge
                    )
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
                        val payment = payments?.getOrNull(pageIndex - 1) ?: PaymentMethod(
                            id = "123",
                            name = "placeholder",
                            color = "FF64B5F6",
                            ownerUserId = "123"
                        )
                        val color = if (payment.inHousehold) Color.Companion.fromHex(
                            payment.color ?: "#cccccc"
                        ) else Color.Gray
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
                            if (pageIndex == 0) {
                                TextButton(
                                    onClick = {
                                        selectedPaymentMethod = null
                                        showDialog = PaymentUiEvent.ADD
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
                                        .padding(top = 24.dp),
                                    horizontalAlignment = Alignment.End
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
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier.defaultMinSize(minWidth = 56.dp)
                                        ) {
                                            if (!payment.editable) {
                                                Row(
                                                    modifier = Modifier
                                                        .wrapContentWidth()
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color.LightGray)
                                                        .padding(4.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) {
                                                    Icon(
                                                        modifier = Modifier.size(18.dp),
                                                        imageVector = FeatherIcons.Eye,
                                                        contentDescription = null,
                                                    )
                                                    Text(
                                                        text = "READ ONLY",
                                                        color = Color.DarkGray,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                            }
                                        }
                                        IconButton(
                                            modifier = Modifier.size(32.dp),
                                            enabled = payment.editable,
                                            onClick = {
                                                showDropdown = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = FeatherIcons.Menu,
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                    DropdownMenu(
                                        modifier = Modifier.align(Alignment.End),
                                        expanded = showDropdown,
                                        onDismissRequest = {
                                            showDropdown = false
                                        }
                                    ) {
                                        if (payment.inHousehold) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(text = "Edit")
                                                },
                                                onClick = {
                                                    showDropdown = false
                                                    selectedPaymentMethod = payment
                                                    showDialog = PaymentUiEvent.UPDATE
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "Delete"
                                                    )
                                                },
                                                onClick = {
                                                    showDropdown = false
                                                    selectedPaymentMethod = payment
                                                    showDialog = PaymentUiEvent.DELETE
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = {
                                                    Text(text = "Remove from Household")
                                                },
                                                onClick = {
                                                    showDropdown = false
                                                    onEvent(
                                                        WalletEvent.RemovePaymentMethodFromHousehold(
                                                            payment.id!!
                                                        )
                                                    )
                                                }
                                            )
                                        } else {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(text = "Add to Household")
                                                },
                                                onClick = {
                                                    showDropdown = false
                                                    onEvent(
                                                        WalletEvent.AddPaymentMethodToHousehold(
                                                            payment.id!!
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Page indicator
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        repeat(pagerState.pageCount) { index ->
                            val color =
                                if (index == pagerState.currentPage) MaterialTheme.colorScheme.onSurface else Color.Gray
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
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                if (first) {
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
            }
        }
        // Transactions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.titleLarge
            )
            UiStateContainer(
                modifier = Modifier.fillMaxWidth(),
                state = state.transactions
            ) { isLoading, transactions ->
                TransactionList(
                    modifier = Modifier.fillMaxWidth(),
                    transactions = transactions,
                    isLoading = isLoading,
                    onDelete = {}
                ) {
                    // TODO
                }
            }
        }
        // Dialog (Add / Edit)
        showDialog?.let { uiEvent ->
            Dialog(
                onDismissRequest = {
                    showDialog = null
                    selectedPaymentMethod = null
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
                        PaymentUiEvent.ADD -> {
                            PaymentMethodForm(
                                modifier = Modifier.fillMaxWidth(),
                                paymentMethod = null,
                                onConfirm = { paymentMethod ->
                                    onEvent(WalletEvent.AddPaymentMethod(paymentMethod))
                                    showDialog = null
                                    selectedPaymentMethod = null
                                }
                            ) {
                                showDialog = null
                            }
                        }
                        PaymentUiEvent.UPDATE -> {
                            PaymentMethodForm(
                                modifier = Modifier.fillMaxWidth(),
                                paymentMethod = selectedPaymentMethod,
                                onConfirm = { paymentMethod ->
                                    onEvent(WalletEvent.UpdatePaymentMethod(paymentMethod))
                                    showDialog = null
                                    selectedPaymentMethod = null
                                }
                            ) {
                                showDialog = null
                                selectedPaymentMethod = null
                            }
                        }
                        PaymentUiEvent.DELETE -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    text = "Are you sure you want to delete this payment method?",
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
                                            selectedPaymentMethod = null
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
                                            onEvent(WalletEvent.DeletePaymentMethod(selectedPaymentMethod!!.id!!))
                                            showDialog = null
                                            selectedPaymentMethod = null
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

enum class PaymentUiEvent {
    ADD,
    UPDATE,
    DELETE
}

@Preview
@Composable
fun WalletScreenPreview() {
    LemonTheme {
        Surface {
            WalletScreen(
                modifier = Modifier.fillMaxSize(),
                state = WalletState(
                    paymentMethods = UiState.Success(
                        listOf(
                            PaymentMethod(
                                id = "1",
                                name = "Credit Card",
                                color = "FF64B5F6",
                                ownerUserId = "1",
                                editable = false
                            ),
                            PaymentMethod(
                                id = "2",
                                name = "Debit Card",
                                color = "FFFFB74D",
                                ownerUserId = "1"
                            ),
                            PaymentMethod(
                                id = "3",
                                name = "Cash",
                                color = "FF81C784",
                                ownerUserId = "1"
                            )
                        )
                    ),
                    transactions = UiState.Success(
                        mapOf(
                            "2025-01-01" to listOf(
                                TransactionItem(
                                    id = "1",
                                    type = TransactionType.EXPENSE,
                                    title = "Shopping",
                                    amount = 200000.0,
                                    date = "2025-01-01",
                                    categoryName = "Shopping",
                                    color = "#cccccc",
                                    icon = "fork",
                                )
                            )
                        )
                    )
                )
            ) {}
        }
    }
}