package com.srnyndrs.android.lemon.ui.components.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.ui.components.transaction.Transaction
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.Minus
import compose.icons.feathericons.Plus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    payments: List<PaymentMethod>,
    onConfirm: (Transaction) -> Unit
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0) { 3 }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val convertMillisToDate = { millis: Long ->
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        formatter.format(Date(millis))
    }

    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }
    val options = listOf("Income", "Expense")
    var transactionAmount by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var transactionName by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    var expanded by remember { mutableStateOf(false) }

    val validateAmount = (transactionAmount.text.isNotBlank() && transactionAmount.text.toFloatOrNull() == null)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.then(modifier)
            .padding(vertical = 12.dp, horizontal = 6.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title
        Text(
            text = "Add Transaction",
            style = MaterialTheme.typography.headlineMedium,
        )
        // Content
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when(page) {
                0 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Income / Expense selector
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Transaction Type",
                                style = MaterialTheme.typography.titleMedium
                            )
                            SingleChoiceSegmentedButtonRow(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                options.forEachIndexed { index, label ->
                                    SegmentedButton(
                                        /*shape = SegmentedButtonDefaults.itemShape(
                                            index = index,
                                            count = options.size
                                        ),*/
                                        shape = RectangleShape,
                                        onClick = { selectedIndex = index },
                                        selected = index == selectedIndex,
                                        label = {
                                            Text(label)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == 1) FeatherIcons.ArrowDown else FeatherIcons.ArrowUp,
                                                contentDescription = null
                                            )
                                        },
                                        colors = SegmentedButtonDefaults.colors(
                                            // TODO: custom color
                                            activeContainerColor = if (index == 1) MaterialTheme.colorScheme.errorContainer else Color.fromHex(
                                                "4CAF50"
                                            ),
                                            inactiveContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                                            activeContentColor = if (index == 1) MaterialTheme.colorScheme.onErrorContainer else Color.White,
                                        )
                                    )

                                }
                            }
                        }
                        // Amount
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Transaction Amount",
                                style = MaterialTheme.typography.titleMedium
                            )
                            // TODO: custom amount input
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                value = transactionAmount,
                                onValueChange = { transactionAmount = it },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                ),
                                prefix = {
                                    Text(
                                        modifier = Modifier.padding(end = 12.dp),
                                        text = "HUF",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                textStyle = MaterialTheme.typography.titleLarge.copy(
                                    //textAlign = TextAlign.Center,
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                isError = validateAmount
                            )
                        }
                        // Name
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Transaction Name",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = transactionName,
                                onValueChange = { transactionName = it },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                ),
                                placeholder = {
                                    Text(
                                        text = "Shopping",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                singleLine = true,
                            )
                        }
                        // Transaction date
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Transaction Date",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .requiredHeight(64.dp)
                                        .focusRequester(focusRequester),
                                    value = selectedDate,
                                    onValueChange = { },
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { showDatePicker = !showDatePicker }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = "Select date"
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    singleLine = true,
                                )

                                if (showDatePicker) {
                                    Popup(
                                        onDismissRequest = { showDatePicker = false },
                                        alignment = Alignment.TopStart
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .offset(y = 64.dp)
                                                .shadow(elevation = 4.dp)
                                                .background(MaterialTheme.colorScheme.surface)
                                                .padding(16.dp)
                                        ) {
                                            DatePicker(
                                                state = datePickerState,
                                                showModeToggle = false
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Payment method selector

                        // Category selector

                    }
                }
                2 -> {
                    // Notes, date, confirm
                    Column {
                        Text(text = "Done")
                    }
                }
            }
        }
        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = {
                    focusRequester.requestFocus()
                    scope.launch {
                        if(pagerState.currentPage == pagerState.pageCount - 1) {
                            // TODO: validate and submit


                            // TODO: clear form after submission

                        } else {
                             pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            ) {
                Text(
                    text = if(pagerState.currentPage == pagerState.pageCount - 1) "Confirm" else "Next",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionFormPreview() {
    LemonTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                //.requiredHeight(480.dp)
        ) {
            TransactionForm(
                modifier = Modifier,
                categories = emptyList(),
                payments = emptyList(),
            ) {}
        }
    }
}