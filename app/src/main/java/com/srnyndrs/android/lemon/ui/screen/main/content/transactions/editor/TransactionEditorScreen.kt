package com.srnyndrs.android.lemon.ui.screen.main.content.transactions.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.components.forms.TransactionForm
import com.srnyndrs.android.lemon.ui.screen.main.MainUiEvent
import com.srnyndrs.android.lemon.ui.utils.UiState

@Composable
fun TransactionEditorScreen(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    onBack: () -> Unit,
    onEvent: (TransactionEditorEvent) -> Unit,
) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TODO: Better state handling
        if(state.categories is UiState.Success && state.paymentMethods is UiState.Success && state.transaction is UiState.Success) {
            TransactionForm(
                modifier = Modifier.fillMaxSize(),
                transaction = state.transaction.data,
                categories = state.categories.data,
                payments = state.paymentMethods.data,
            ) {
                onEvent(
                    TransactionEditorEvent.AddTransaction(
                        transaction = it
                    )
                )
                onBack()
            }
        } else {
            CircularProgressIndicator()
        }
    }

}