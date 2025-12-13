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
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.domain.genai.BillItem
import com.srnyndrs.android.lemon.ui.components.forms.TransactionForm
import com.srnyndrs.android.lemon.ui.screen.scan.ScanEvent
import com.srnyndrs.android.lemon.ui.screen.scan.ScanScreen
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillViewModel
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun BillResultScreen(
    modifier: Modifier = Modifier,
    uiState: SplitBillUiState,
    categories: List<Category>,
    payments: List<PaymentMethod>,
    onEvent: (ScanEvent) -> Unit,
    onSaved: () -> Unit = {},
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                is SplitBillUiState.Saving -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Saving transaction...")
                    }
                }
                is SplitBillUiState.Saved -> {
                    // Navigate back or to main screen via callback
                    onSaved()
                }
                is SplitBillUiState.Success -> {
                    TransactionForm(
                        modifier = Modifier.fillMaxSize(),
                        transaction = state.transactionDetails,
                        categories = categories,
                        payments = payments,
                    ) {
                        onEvent(ScanEvent.SaveTransaction(it))
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

@Preview
@Composable
fun BillResultScreenPreview() {
    LemonTheme {
        Surface {
            BillResultScreen(
                modifier = Modifier.fillMaxSize(),
                    uiState = SplitBillUiState.Success(
                        transactionDetails = TransactionDetailsDto(
                            title = "Sample Restaurant",
                            amount = 123.45,
                            date = "2024-06-01",
                        )
                    ),
                categories = emptyList(),
                payments = emptyList(),
                onEvent = {}
            ) {}
        }
    }
}
