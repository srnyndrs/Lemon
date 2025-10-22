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
fun LoginForm(
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
) {

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    val isFormValid = emailValue.isNotBlank() && passwordValue.isNotBlank()

    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Title
        Text(
            text = "Log in to your account",
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
        }
        // Submit Button
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
                text = "Log In",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@PreviewLightDark
@Composable
fun LoginFormPreview() {
    LemonTheme {
        Surface {
            LoginForm(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                onSubmit = { email, password -> }
            )
        }
    }
}