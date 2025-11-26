package com.srnyndrs.android.lemon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import com.srnyndrs.android.lemon.ui.utils.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun <T> UiStateContainer(
    modifier: Modifier = Modifier,
    state: UiState<T>,
    content: @Composable (loading: Boolean, data: T?) -> Unit
) {

    Column(
        modifier = Modifier.then(modifier)
    ) {
        when(state) {
            is UiState.Loading -> {
                content(true, null)
            }
            is UiState.Success -> {
                content(false, state.data)
            }
            is UiState.Error -> {
                // TODO: Show error state
                Text(text = state.message)
            }
            else -> {}
        }
    }

}

@Preview
@Composable
private fun UiStateContainerPreview() {
    LemonTheme {
        Surface {

            val scope = rememberCoroutineScope()
            var state by remember { mutableStateOf<UiState<String>>(UiState.Empty()) }

            LaunchedEffect(Unit) {
                scope.launch {
                    state = UiState.Loading()
                    delay(5000)
                    state = UiState.Success("Success")
                }
            }

            UiStateContainer(
                modifier = Modifier.fillMaxSize().padding(6.dp),
                state = state
            ) { isLoading, data ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp)
                        .background(Color.Red)
                        .let {
                            if(isLoading) {
                                it.shimmer()
                            } else it
                        }
                ) {
                    data?.let {
                        Text(
                            text = it
                        )
                    }
                }
            }
        }
    }
}