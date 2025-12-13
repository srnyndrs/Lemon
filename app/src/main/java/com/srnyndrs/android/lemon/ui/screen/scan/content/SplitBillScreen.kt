package com.srnyndrs.android.lemon.ui.screen.scan.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillViewModel

@Composable
fun SplitBillScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SplitBillViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val people by viewModel.people.collectAsState()
    val itemAssignments by viewModel.itemAssignments.collectAsState()
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = { showResult = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Calculate Split")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                AddPersonSection(onAddPerson = { viewModel.addPerson(it) })
                Spacer(modifier = Modifier.height(16.dp))
                Text("Assign Items to People", style = MaterialTheme.typography.headlineSmall)
            }

            if (uiState is SplitBillUiState.Success) {
                val billItems = (uiState as SplitBillUiState.Success).billDetails.items
                items(billItems) { item ->
                    ItemAssignmentCard(
                        item = item,
                        people = people,
                        assignedPeople = itemAssignments[item] ?: emptyList(),
                        onAssignPerson = { person ->
                            viewModel.assignItemToPerson(item, person)
                        }
                    )
                }
            }
        }

        if (showResult) {
            val splitResult = viewModel.getSplitBillResult()
            ResultDialog(
                result = splitResult,
                onDismiss = { showResult = false }
            )
        }
    }
}

@Composable
fun AddPersonSection(onAddPerson: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Person's Name") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = {
            onAddPerson(name)
            name = ""
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add Person")
        }
    }
}

@Composable
fun ItemAssignmentCard(
    item: BillItem,
    people: List<String>,
    assignedPeople: List<String>,
    onAssignPerson: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Price: ${item.totalPrice}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            people.forEach { person ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAssignPerson(person) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = assignedPeople.contains(person),
                        onCheckedChange = { onAssignPerson(person) }
                    )
                    Text(text = person)
                }
            }
        }
    }
}

@Composable
fun ResultDialog(result: Map<String, Double>, onDismiss: () -> Unit) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Split Result", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                result.forEach { (person, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = person)
                        Text(text = String.format("%.2f", amount))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Close")
                }
            }
        }
    }
}