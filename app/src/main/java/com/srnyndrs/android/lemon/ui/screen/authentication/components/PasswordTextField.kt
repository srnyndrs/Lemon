package com.srnyndrs.android.lemon.ui.screen.authentication.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.Key
import compose.icons.feathericons.Lock

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    var hidePassword by remember { mutableStateOf(true) }

    val visualTransformation =
        if (hidePassword) PasswordVisualTransformation()
        else VisualTransformation.None

    OutlinedTextField(
        modifier = Modifier.then(modifier),
        colors = OutlinedTextFieldDefaults.colors(),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = title)
        },
        label = {
            Text(text = title)
        },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = FeatherIcons.Lock, // Icons.Default.Lock
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { hidePassword = !hidePassword }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if(hidePassword) FeatherIcons.EyeOff else FeatherIcons.Eye,
                    contentDescription = null
                )
            }
        },
        visualTransformation = visualTransformation,
    )
}

@PreviewLightDark
@Composable
fun PasswordTextFieldPreview() {
    LemonTheme {
        Surface {
            PasswordTextField(
                value = "123abc123",
                title = "Password"
            ) { }
        }
    }
}