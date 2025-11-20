package com.srnyndrs.android.lemon.ui.screen.main.content.household

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check
import compose.icons.feathericons.Home
import compose.icons.feathericons.UserCheck
import compose.icons.feathericons.UserX

@Composable
fun HouseholdScreen(
    modifier: Modifier = Modifier
) {

    var householdName by remember {
        // TODO: use selected household name
        mutableStateOf(TextFieldValue("Private Household"))
    }

    var isEditMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = FeatherIcons.Home,
                contentDescription = null
            )
            TextField(
                value = householdName,
                onValueChange = {
                    householdName = it
                },
                enabled = isEditMode,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.headlineSmall
            )
            if(isEditMode) {
                IconButton(
                    onClick = {
                        isEditMode = false
                        // TODO: save changes
                    }
                ) {
                    Icon(
                        imageVector = FeatherIcons.Check,
                        contentDescription = null
                    )
                }
            }
        }
        // Members
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Members",
                style = MaterialTheme.typography.bodyLarge,
            )
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
                                // TODO: remove user from household
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
        }
        // Actions
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
                    isEditMode = true
                },
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

@Preview
@Composable
fun HouseholdScreenPreview() {
    LemonTheme {
        Surface {
            HouseholdScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            )
        }
    }
}