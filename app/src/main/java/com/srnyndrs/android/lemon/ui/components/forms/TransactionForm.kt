package com.srnyndrs.android.lemon.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.fromHex
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier
) {

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("Income", "Expense")

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) },
                    icon = {
                        Icon(
                            imageVector = if(index == 1) FeatherIcons.ArrowDown else FeatherIcons.ArrowUp,
                            contentDescription = null
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(
                        // TODO: custom color
                        activeContainerColor = if(index == 1) MaterialTheme.colorScheme.errorContainer else Color.fromHex("4CAF50"),
                        inactiveContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        activeContentColor = if(index == 1) MaterialTheme.colorScheme.onErrorContainer else Color.White,
                    )
                )
            }
        }
        //

    }
}

@PreviewLightDark
@Composable
fun TransactionFormPreview() {
    LemonTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            TransactionForm(
                modifier = Modifier
            )
        }
    }
}