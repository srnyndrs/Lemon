package com.srnyndrs.android.lemon.ui.components.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.domain.database.model.PaymentMethod
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun HouseholdForm(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
) {

    var householdName by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.then(modifier)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add Household",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(0.33f),
                            RectangleShape
                        ),
                    value = householdName,
                    placeholder = {
                        Text(
                            text = "e.g. Family Expenses",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    onValueChange = { householdName = it },
                    singleLine = true,
                    shape = RoundedCornerShape(5.dp)
                )
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
                    if(householdName.text.isNotBlank()) {
                        onConfirm(householdName.text)
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
fun HouseholdFormPreview() {
    LemonTheme {
        Surface {
            HouseholdForm(
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = {}
            ) {}
        }
    }
}
