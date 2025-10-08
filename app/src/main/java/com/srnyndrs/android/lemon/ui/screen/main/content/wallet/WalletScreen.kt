package com.srnyndrs.android.lemon.ui.screen.main.content.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Wallet Screen")
    }
}

@PreviewLightDark
@Composable
fun WalletScreenPreview() {
    LemonTheme {
        Surface {
            WalletScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}