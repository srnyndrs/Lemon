package com.srnyndrs.android.lemon.ui.components.selectors

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
    selectedItem: Int?,
    paymentMethods: List<PaymentMethod>,
    onSelect: (Int?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.then(modifier)
            .requiredHeight(42.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Card(
                modifier = Modifier.requiredHeight(42.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onSurface.copy(0.3f)
                ),
                onClick = {
                    onSelect(null)
                },
                border = BorderStroke(
                    width = 2.dp,
                    color = if (selectedItem == null) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        Color.Transparent
                    }
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NONE",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        itemsIndexed(paymentMethods) { index, paymentMethod ->
            Card(
                modifier = Modifier.requiredHeight(42.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.fromHex(paymentMethod.color ?: "#BBDEFB")
                ),
                onClick = {
                    onSelect(index)
                },
                border = BorderStroke(
                    width = 2.dp,
                    color = if (selectedItem == index) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        Color.Transparent
                    }
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
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
            var selectedIndex by remember { mutableStateOf<Int?>(null) }
            PaymentMethodSelector(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
                selectedItem = selectedIndex,
                paymentMethods = listOf(
                    PaymentMethod(id = "1", name = "Cash", color = "#FFCDD2", ownerUserId = "1"),
                    PaymentMethod(id = "2", name = "Card", color = "#C8E6C9", ownerUserId = "1" ),
                    PaymentMethod(id = "3", name = "Wallet", color = "#BBDEFB", ownerUserId = "1" ),
                )
            ) { selectedIndex = it }
        }
    }
}