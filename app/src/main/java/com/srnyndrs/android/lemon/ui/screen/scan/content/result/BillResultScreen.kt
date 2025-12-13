package com.srnyndrs.android.lemon.ui.screen.scan.content.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.srnyndrs.android.lemon.domain.genai.BillItem
import com.srnyndrs.android.lemon.ui.screen.scan.Screen
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillViewModel

@Composable
fun BillResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SplitBillViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = {
                    navController.navigate(Screen.SplitBillScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 64.dp, start = 16.dp, end = 16.dp)
            ) {
                Text("Confirm and Split Bill")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (val state = uiState) {
                is SplitBillUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SplitBillUiState.Success -> {
                    var editableBillDetails by remember(state.billDetails) {
                        mutableStateOf(state.billDetails)
                    }

                    LazyColumn( // LazyColumn provides scrolling automatically when content overflows
                        modifier = Modifier.fillMaxSize() // Fill available space given by parent Column
                    ) {
                        item {
                            Text(
                                "Edit Bill Details",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }

                        item {
                            EditableBillField(
                                label = "Restaurant Name",
                                value = editableBillDetails.restaurantName,
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(restaurantName = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item {
                            EditableBillField(
                                label = "Date & Time",
                                value = editableBillDetails.dateTime, // Assuming dateTime exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(dateTime = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        item { Text("Items:", style = MaterialTheme.typography.titleMedium) }
                        item { Spacer(modifier = Modifier.height(8.dp)) }


                        items(editableBillDetails.items) { billItem ->
                            EditableBillItem(
                                item = billItem,
                                onValueChange = { updatedItem ->
                                    val updatedItems = editableBillDetails.items.map {
                                        if (it.name == billItem.name && it.totalPrice == billItem.totalPrice) { // Or use a unique ID if items can have same name/price
                                            updatedItem
                                        } else {
                                            it
                                        }
                                    }
                                    editableBillDetails = editableBillDetails.copy(items = updatedItems)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp)) // Spacer between items
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        item { Text("Summary:", style = MaterialTheme.typography.titleMedium) }
                        item { Spacer(modifier = Modifier.height(8.dp)) }


                        item {
                            EditableBillField(
                                label = "Subtotal",
                                value = editableBillDetails.subTotal, // Assuming subTotal exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(subTotal = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item {
                            EditableBillField(
                                label = "Tax",
                                value = editableBillDetails.tax, // Assuming tax exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(tax = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item {
                            EditableBillField(
                                label = "Service Charge",
                                value = editableBillDetails.service, // Assuming service exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(service = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item {
                            EditableBillField(
                                label = "Discount",
                                value = editableBillDetails.discount, // Assuming discount exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(discount = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }
                        item {
                            EditableBillField(
                                label = "Other Fees",
                                value = editableBillDetails.others, // Assuming others exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(others = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item {
                            EditableBillField(
                                label = "Total",
                                value = editableBillDetails.total, // Assuming total exists
                                onValueChange = { newValue ->
                                    editableBillDetails = editableBillDetails.copy(total = newValue)
                                    viewModel.updateBillDetails(editableBillDetails)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                is SplitBillUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is SplitBillUiState.Idle -> { // Handle Idle state, perhaps show a message or different UI
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No bill data loaded yet.")
                    }
                }
            }
        }
    }
}

@Composable
fun EditableBillField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
fun EditableBillItem(item: BillItem, onValueChange: (BillItem) -> Unit) {
    var name by remember(item.name) { mutableStateOf(item.name) }
    var quantity by remember(item.quantity) { mutableStateOf(item.quantity) }
    var totalPrice by remember(item.totalPrice) { mutableStateOf(item.totalPrice) }


    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                onValueChange(item.copy(name = it, quantity = quantity, totalPrice = totalPrice))
            },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it
                    onValueChange(item.copy(name = name, quantity = it, totalPrice = totalPrice))
                },
                label = { Text("Quantity") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = totalPrice,
                onValueChange = {
                    totalPrice = it
                    onValueChange(item.copy(name = name, quantity = quantity, totalPrice = it))
                },
                label = { Text("Total Price") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}