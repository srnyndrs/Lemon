package com.srnyndrs.android.lemon.ui.components.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.ui.components.colors.ColorPicker
import com.srnyndrs.android.lemon.ui.components.icons.IconPicker
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.DollarSign

@Composable
fun PaymentMethodForm(
    modifier: Modifier = Modifier,
    onConfirm: (PaymentMethod) -> Unit,
    onDismissRequest: () -> Unit,
) {

    var paymentMethodName by remember { mutableStateOf(TextFieldValue()) }

    var selectedColor by remember { mutableStateOf<String>("#BBDEFB") }
    var selectedIcon by remember { mutableStateOf(FeatherIcons.DollarSign) }

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add Payment",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleMedium
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = paymentMethodName,
                    label = {
                        Text(
                            text = "Payment Name",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    onValueChange = { paymentMethodName = it },
                    singleLine = true,
                    shape = RoundedCornerShape(5.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Color",
                    style = MaterialTheme.typography.titleMedium
                )
                ColorPicker(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedColor = it
                }
            }
        }
        // Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(42.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
        ) {
            TextButton(
                modifier = Modifier.weight(0.5f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "Cancel"
                )
            }
            VerticalDivider(
                color = MaterialTheme.colorScheme.surface,
                thickness = 1.dp
            )
            TextButton(
                modifier = Modifier.weight(0.5f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    // validation
                    if(paymentMethodName.text.isNotBlank()) {
                        val paymentMethod = PaymentMethod(
                            name = paymentMethodName.text.trim(),
                            color = selectedColor,
                        )
                        onConfirm(paymentMethod)
                    }
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "Done"
                )
            }
        }
    }
}

@Preview
@Composable
fun PaymentMethodFormPreview() {
    LemonTheme {
        Surface {
            PaymentMethodForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onConfirm = {}
            ) {}
        }
    }
}