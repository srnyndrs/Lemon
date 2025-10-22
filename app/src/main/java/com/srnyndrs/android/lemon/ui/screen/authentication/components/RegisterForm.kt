package com.srnyndrs.android.lemon.ui.screen.authentication.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
) {

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var password2Value by remember { mutableStateOf("") }

    val isFormValid = emailValue.isNotBlank() &&
            passwordValue.isNotBlank() &&
            passwordValue == password2Value

    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Title
        Text(
            text = "Create an account",
            style = MaterialTheme.typography.headlineMedium
        )
        // Form
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Email field
            EmailTextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailValue
            ) { newValue ->
                emailValue = newValue
            }
            // Password field
            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                title = "Password",
                value = passwordValue
            ) { newValue ->
                passwordValue = newValue
            }
            // Password check field
            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                title = "Confirm Password",
                value = password2Value
            ) { newValue ->
                password2Value = newValue
            }
        }
        //
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                if(isFormValid) {
                    onSubmit(emailValue, passwordValue)
                }
            }
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RegisterFormPreview() {
    LemonTheme {
        Surface {
            RegisterForm(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                onSubmit = { _, _ -> }
            )
        }
    }
}