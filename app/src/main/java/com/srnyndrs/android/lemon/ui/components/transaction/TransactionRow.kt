package com.srnyndrs.android.lemon.ui.components.transaction

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Transaction
import com.srnyndrs.android.lemon.domain.database.model.TransactionItem
import com.srnyndrs.android.lemon.domain.database.model.TransactionType
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.formatAsCurrency
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.Book
import compose.icons.feathericons.Minus
import compose.icons.feathericons.Plus

// TODO: refactor


@Composable
fun TransactionRow(
    modifier: Modifier = Modifier,
    transaction: TransactionItem,
    onClick: () -> Unit
) {

    val backgroundHexColor = transaction.color ?: when(transaction.type) {
        TransactionType.EXPENSE -> "#CCCCCC"
        TransactionType.INCOME -> "#C8E6CF"
    }

    Row(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp,MaterialTheme.colorScheme.onSurface.copy(0.05f), RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp, horizontal = 6.dp),
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
                    .background(Color.fromHex(backgroundHexColor).copy(0.4f))
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if(transaction.icon != null) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = FeatherIcons.Book,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = if (transaction.type == TransactionType.EXPENSE) FeatherIcons.ArrowDown else FeatherIcons.ArrowUp,
                        contentDescription = null
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = transaction.categoryName ?: transaction.type.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val isExpense = transaction.type == TransactionType.EXPENSE
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = if(isExpense) FeatherIcons.Minus else FeatherIcons.Plus,
                //tint = if(isExpense) Color.Red else Color.Green,
                contentDescription = null
            )
            // TODO: Format amount
            Text(
                text = "${transaction.amount.formatAsCurrency()} Ft",
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
                transaction = TransactionItem(
                    id = "",
                    title = "Grocery store",
                    type = TransactionType.EXPENSE,
                    amount = 5000.0,
                    date = "2024-06-01",
                    //color = "#FF5733",
                    categoryName = "Shopping",
                    icon = null
                )
            ) {

            }
        }
    }
}