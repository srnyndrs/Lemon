package com.srnyndrs.android.lemon.ui.components.selectors

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex

@Composable
fun PaymentMethodSelector(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    paymentMethods: List<PaymentMethod>,
    onSelect: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier.then(modifier)
            .requiredHeight(56.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(paymentMethods) { index, paymentMethod ->
            Card(
                modifier = Modifier
                    .requiredHeight(56.dp)
                    .border(2.dp, if (selectedItem == index) MaterialTheme.colorScheme.onSurface else Color.Transparent, CardDefaults.shape),
                    //.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CardDefaults.shape),
                colors = CardDefaults.cardColors(
                    containerColor = Color.fromHex(paymentMethod.color)
                ),
                onClick = {
                    onSelect(index)
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 3.dp, horizontal = 6.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier,
                        text = paymentMethod.name.uppercase(),
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PaymentMethodSelectorPreview() {
    LemonTheme {
        Surface {
            var selectedIndex by remember { mutableIntStateOf(0) }
            PaymentMethodSelector(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
                selectedItem = selectedIndex,
                paymentMethods = listOf(
                    PaymentMethod(id = "1", name = "Cash", color = "#FFCDD2" ),
                    PaymentMethod(id = "2", name = "Card", color = "#C8E6C9" ),
                    PaymentMethod(id = "3", name = "Wallet", color = "#BBDEFB" ),
                )
            ) { selectedIndex = it }
        }
    }
}