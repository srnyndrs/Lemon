package com.srnyndrs.android.lemon.ui.screen.main.content.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.srnyndrs.android.lemon.ui.components.ActionButton
import com.srnyndrs.android.lemon.ui.components.forms.CategoryForm
import com.srnyndrs.android.lemon.ui.components.forms.HouseholdForm
import com.srnyndrs.android.lemon.ui.components.forms.PaymentMethodForm
import com.srnyndrs.android.lemon.ui.screen.main.MainEvent
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp
import compose.icons.feathericons.Delete
import compose.icons.feathericons.Edit2
import compose.icons.feathericons.Home
import compose.icons.feathericons.User
import compose.icons.feathericons.UserCheck
import compose.icons.feathericons.UserX

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    username: String,
    email: String,
    onMainEvent: (MainEvent<*>) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .then(modifier)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(0.4f))
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = FeatherIcons.User,
                    contentDescription = null
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                title = "Edit Profile",
                icon = FeatherIcons.Edit2,
            ) { }
            ActionButton(
                title = "Create Household",
                icon = FeatherIcons.Home,
            ) {
                showDialog = true
            }
            ActionButton(
                title = "Sign out",
                icon = FeatherIcons.Delete,
            ) {
                onMainEvent(MainEvent.Logout)
            }
        }
        // Dialog
        if(showDialog) {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
                ) {
                    HouseholdForm(
                        modifier = Modifier.fillMaxWidth(),
                        onDismissRequest = { showDialog = false },
                    ) { householdName ->
                        onMainEvent(MainEvent.CreateHousehold(householdName))
                        showDialog = false
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    LemonTheme {
        Surface {
            ProfileScreen(
                modifier = Modifier.fillMaxSize(),
                username = "John Doe",
                email = "johndoe@example.com",
            ) {  }
        }
    }
}