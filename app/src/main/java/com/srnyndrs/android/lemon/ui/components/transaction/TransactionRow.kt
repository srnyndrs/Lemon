package com.srnyndrs.android.lemon.ui.components.transaction

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.CreditCard
import compose.icons.feathericons.Minus
import compose.icons.feathericons.Plus

// TODO: refactor
data class Transaction(
    val id: String,
    val name: String,
    val transactionType: TransactionType,
    val amount: Int
)

enum class TransactionType {
    INCOME, EXPENSE
}

@Composable
fun TransactionRow(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp, horizontal = 6.dp),
            //.shadow(1.dp, RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = FeatherIcons.CreditCard,
                    contentDescription = null
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Jan 1, 2024", // TODO: date
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val isExpense = transaction.transactionType == TransactionType.EXPENSE
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = if(isExpense) FeatherIcons.Minus else FeatherIcons.Plus,
                //tint = if(isExpense) Color.Red else Color.Green,
                contentDescription = null
            )
            Text(
                text = "${transaction.amount} Ft",
                //color = if(isExpense) Color.Red else Color.Green,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@PreviewLightDark
@Composable
fun TransactionRowPreview() {
    LemonTheme {
        Surface {
            TransactionRow(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
                transaction = Transaction(
                    id = "",
                    name = "Grocery store",
                    transactionType = TransactionType.EXPENSE,
                    amount = 5000,
                )
            ) {

            }
        }
    }
}