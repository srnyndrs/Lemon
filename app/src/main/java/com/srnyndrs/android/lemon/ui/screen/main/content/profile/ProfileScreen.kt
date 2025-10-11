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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.srnyndrs.android.lemon.ui.components.ActionButton
import com.srnyndrs.android.lemon.ui.screen.main.MainEvent
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp
import compose.icons.feathericons.Delete
import compose.icons.feathericons.Edit
import compose.icons.feathericons.Edit2
import compose.icons.feathericons.Home
import compose.icons.feathericons.User
import compose.icons.feathericons.UserCheck
import compose.icons.feathericons.UserPlus
import compose.icons.feathericons.UserX

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Screen",
            style = MaterialTheme.typography.titleLarge
        )

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
                    text = "username",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "johndoe@gmail.com",
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
            ) { }
            ActionButton(
                title = "Sign out",
                icon = FeatherIcons.Delete,
            ) { }
        }
        // Households
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = "Households",
                style = MaterialTheme.typography.headlineSmall
            )
            // Household list
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) {
                    var isExpanded by remember { mutableStateOf(false) }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(0.2f))
                            .padding(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Household #${it + 1}")
                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = {
                                    isExpanded = !isExpanded
                                }
                            ) {
                                Icon(
                                    imageVector = if(isExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                    contentDescription = null
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = isExpanded
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                repeat(2) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.onSurface.copy(0.4f))
                                                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(14.dp),
                                                    imageVector = FeatherIcons.UserCheck,
                                                    contentDescription = null
                                                )
                                            }
                                            Text(
                                                text = "Member #${it + 1}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                        // User actions
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    // TODO
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = FeatherIcons.UserX,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Edit
                                    TextButton(
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(0.8f)
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        onClick = {
                                            // TODO: Edit household
                                        }
                                    ) {
                                        Text(
                                            text = "Edit Household",
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                    // Delete
                                    TextButton(
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = MaterialTheme.colorScheme.error.copy(0.8f)
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        onClick = {
                                            // TODO: Delete household
                                        }
                                    ) {
                                        Text(
                                            text = "Delete Household",
                                            color = MaterialTheme.colorScheme.onError
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }


        Button(
            onClick = { onLogout() }
        ) {
            Text(text = "Sign Out")
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    LemonTheme {
        Surface {
            ProfileScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            ) {  }
        }
    }
}