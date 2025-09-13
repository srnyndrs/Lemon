package com.srnyndrs.android.lemon.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun CustomTextFieldWithIcons(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: Painter? = null,
    onTrailingIconClick: () -> Unit = {},
    trailingIcon: Painter? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            if (leadingIcon != null)
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = leadingIcon,
                    contentDescription = null
                )
        },
        trailingIcon = {
            if (trailingIcon != null)
                IconButton(onClick = onTrailingIconClick) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = trailingIcon,
                        contentDescription = null
                    )
                }
        }
    )
}

@PreviewLightDark
@Composable
fun CustomTextFieldWithIconsPreview() {
    LemonTheme {
        Surface {
            CustomTextFieldWithIcons(
                modifier = Modifier.padding(16.dp),
                value = "Hello, World!",
                onValueChange = {}
            )
        }
    }
}