package com.srnyndrs.android.lemon.ui.screen.authentication.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Mail

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    value: String,
    imeAction: ImeAction = ImeAction.Default,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.then(modifier),
        colors = OutlinedTextFieldDefaults.colors(),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = "Email")
        },
        label = {
            Text(text = "Email")
        },
        leadingIcon = {
           Icon(
               modifier = Modifier.size(20.dp),
               imageVector = FeatherIcons.Mail,
               contentDescription = null
           )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.None,
            imeAction = imeAction
        ),
        //keyboardActions = KeyboardActions.Default.onNext
    )
}

@PreviewLightDark
@Composable
fun EmailTextFieldPreview() {
    LemonTheme {
        Surface {
            EmailTextField(
                value = "test@test.com"
            ) { }
        }
    }
}