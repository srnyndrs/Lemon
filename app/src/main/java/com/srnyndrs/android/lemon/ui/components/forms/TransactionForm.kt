package com.srnyndrs.android.lemon.ui.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.ui.components.selectors.CategorySelector
import com.srnyndrs.android.lemon.ui.components.selectors.PaymentMethodSelector
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import com.srnyndrs.android.lemon.ui.utils.toMillis
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    transaction: TransactionDetailsDto,
    categories: List<Category>,
    payments: List<PaymentMethod>,
    onConfirm: (TransactionDetailsDto) -> Unit
) {

    val scope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val convertMillisToDate = { millis: Long ->
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.format(Date(millis))
    }

    var selectedTypeIndex by rememberSaveable {
        mutableIntStateOf(if(transaction.type == TransactionType.INCOME) 0 else 1)
    }
    val options = listOf("Income", "Expense")
    var transactionAmount by remember {
        mutableStateOf(
            TextFieldValue(
                if(transaction.amount != 0.0) "%.0f".format(transaction.amount) else ""
            )
        )
    }

    var transactionName by remember {
        mutableStateOf(
            TextFieldValue(
                transaction.title
            )
        )
    }

    var transactionDetails by remember {
        mutableStateOf(
            TextFieldValue(
                transaction.description ?: ""
            )
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = transaction.date?.toMillis() ?: System.currentTimeMillis()
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    var selectedPaymentIndex by rememberSaveable {
        mutableIntStateOf(
            max(0,payments.indexOf(payments.find { it.id == transaction.paymentMethodId }))
        )
    }

    var selectedCategoryIndex by rememberSaveable {
        mutableIntStateOf(
            max(0,categories.indexOf(categories.find { it.id == transaction.categoryId }))
        )
    }

    val validateAmount = (transactionAmount.text.isNotBlank() && transactionAmount.text.toFloatOrNull() == null)

    val scrollState = rememberScrollState()

    val validateForm: () -> Boolean = {
        var isValid = true
        if (transactionAmount.text.isBlank() || transactionAmount.text.toFloatOrNull() == null) {
            isValid = false
        }
        if (transactionName.text.isBlank()) {
            isValid = false
        }
        if (selectedTypeIndex == 1 && categories.isEmpty()) {
            isValid = false
        }
        isValid
    }

    Column(
        modifier = Modifier.then(modifier)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
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
                            onClick = { selectedTypeIndex = index },
                            selected = index == selectedTypeIndex,
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
            //
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.titleMedium
                )
                PaymentMethodSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedItem = selectedPaymentIndex,
                    paymentMethods = payments
                ) {
                    selectedPaymentIndex = it
                }
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Selected date
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
                    // Date picker
                    AnimatedVisibility(showDatePicker) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    DatePickerDefaults.colors().containerColor
                                ),
                        ) {
                            DatePicker(
                                modifier = Modifier.fillMaxWidth(),
                                state = datePickerState,
                                showModeToggle = false
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        showDatePicker = false
                                    }
                                ) {
                                    Text(
                                        text = "Done",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // Optional fields
            var showOptionalFields by rememberSaveable { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = { showOptionalFields = !showOptionalFields }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "More",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Icon(
                            imageVector = if(showOptionalFields) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                            contentDescription = null
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.weight(1f)
                )
            }
            // Optional Fields
            AnimatedVisibility(
                visible = showOptionalFields
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Transaction Details",
                            style = MaterialTheme.typography.titleMedium
                        )
                        // Details
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(128.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(8.dp))
                                .focusRequester(focusRequester) // TODO
                            ,

                            value = transactionDetails,
                            onValueChange = {
                                transactionDetails = it
                            },
                            singleLine = false,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                            ),
                        )
                    }
                    // Category selector
                    AnimatedVisibility(
                        visible = selectedTypeIndex == 1
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Transaction Category",
                                style = MaterialTheme.typography.titleMedium
                            )
                            CategorySelector(
                                modifier = Modifier.fillMaxWidth(),
                                categories = categories,
                                selectedIndex = selectedCategoryIndex
                            ) {
                                selectedCategoryIndex = it
                            }
                        }
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
                        // TODO: validate and submit
                        if(validateForm()) {
                            val transactionDetailsDto = TransactionDetailsDto(
                                title = transactionName.text,
                                amount = transactionAmount.text.toDouble(),
                                type = if(selectedTypeIndex == 1) TransactionType.EXPENSE else TransactionType.INCOME,
                                date = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) },
                                paymentMethodId = payments.getOrNull(selectedPaymentIndex)?.id,
                                categoryId = if(selectedTypeIndex == 1) categories.getOrNull(selectedCategoryIndex)?.id else null,
                                description = transactionDetails.text.ifBlank { null },
                            )
                            onConfirm(transactionDetailsDto)
                        }

                        // TODO: clear form after submission

                    }
                }
            ) {
                Text(
                    text = "Done",
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
                transaction = TransactionDetailsDto(
                    date = "2024-06-15"
                ),
                categories = listOf(
                    Category(
                        id = "1",
                        name = "Shopping",
                        color = "FF5722",
                        icon = "shopping-bag"
                    ),
                    Category(
                        id = "2",
                        name = "Salary",
                        color = "4CAF50",
                        icon = "dollar-sign"
                    ),
                    Category(
                        id = "3",
                        name = "Food",
                        color = "FFC107",
                        icon = "coffee"
                    ),
                ),
                payments = listOf(
                    PaymentMethod(
                        id = "1",
                        name = "Cash",
                        color = "#FFCDD2",
                        ownerUserId = "1"
                    ),
                    PaymentMethod(
                        id = "2",
                        name = "Card",
                        color = "#C8E6C9",
                        ownerUserId = "1"
                    ),
                    PaymentMethod(
                        id = "3",
                        name = "Wallet",
                        color = "#BBDEFB",
                        ownerUserId = "1"
                    ),
                ),
            ) {}
        }
    }
}