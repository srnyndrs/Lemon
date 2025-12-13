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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.domain.genai.BillItem
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillViewModel
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun SplitBillScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SplitBillViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (uiState) {
                is SplitBillUiState.Idle -> {
                    Text("Please scan a bill to split.")
                }
                is SplitBillUiState.Loading -> {
                    Text("Loading...")
                }
                is SplitBillUiState.Error -> {
                    Text("Error: ${(uiState as SplitBillUiState.Error).message}")
                }
                is SplitBillUiState.Success -> {
                    val transactionDetails = (uiState as SplitBillUiState.Success).transactionDetails
                }
                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun SplitBillScreenPreview() {
    LemonTheme {
        Surface {
            SplitBillScreen(
                modifier = Modifier.fillMaxSize(),
                navController = rememberNavController(),
                viewModel = hiltViewModel()
            )
        }
    }
}