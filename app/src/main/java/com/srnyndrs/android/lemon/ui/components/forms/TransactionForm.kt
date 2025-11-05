package com.srnyndrs.android.lemon.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier
) {

    val pagerState = rememberPagerState { 3 }

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("Income", "Expense")
    var transactionAmount by remember { mutableStateOf(
        TextFieldValue("")
    )}

    val validateAmount = (transactionAmount.text.isNotBlank() && transactionAmount.text.toFloatOrNull() == null)

    HorizontalPager(
        modifier = Modifier.then(modifier),
        state = pagerState
    ) { page ->
        when(page) {
            0 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        // TODO: custom amount input
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = transactionAmount,
                            onValueChange = { transactionAmount = it },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.Transparent
                            ),
                            suffix = {
                                Text(
                                    text = "HUF",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Amount",
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                            ),
                            isError = validateAmount
                        )

                    }
                    //
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = options.size
                                ),
                                onClick = { selectedIndex = index },
                                selected = index == selectedIndex,
                                label = { Text(label) },
                                icon = {
                                    Icon(
                                        imageVector = if(index == 1) FeatherIcons.ArrowDown else FeatherIcons.ArrowUp,
                                        contentDescription = null
                                    )
                                },
                                colors = SegmentedButtonDefaults.colors(
                                    // TODO: custom color
                                    activeContainerColor = if(index == 1) MaterialTheme.colorScheme.errorContainer else Color.fromHex("4CAF50"),
                                    inactiveContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                                    activeContentColor = if(index == 1) MaterialTheme.colorScheme.onErrorContainer else Color.White,
                                )
                            )
                        }
                    }
                }
            }
            1 -> {
                // Expense Form
            }
            2 -> {
                // Transfer Form
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionFormPreview() {
    LemonTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            TransactionForm(
                modifier = Modifier
            )
        }
    }
}