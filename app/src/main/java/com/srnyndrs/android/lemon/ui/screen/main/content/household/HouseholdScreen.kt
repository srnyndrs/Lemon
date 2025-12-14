package com.srnyndrs.android.lemon.ui.screen.main.content.household

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.sp
import com.srnyndrs.android.lemon.domain.database.model.Category
import com.srnyndrs.android.lemon.domain.database.model.Household
import com.srnyndrs.android.lemon.domain.database.model.Member
import com.srnyndrs.android.lemon.ui.components.UiStateContainer
import com.srnyndrs.android.lemon.ui.screen.main.MainEvent
import com.srnyndrs.android.lemon.ui.screen.main.MainUiEvent
import com.srnyndrs.android.lemon.ui.screen.main.components.RemotePicture
import com.srnyndrs.android.lemon.ui.screen.main.content.category.CategoryUiEvent
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.Check
import compose.icons.feathericons.Home
import compose.icons.feathericons.Plus
import compose.icons.feathericons.User
import compose.icons.feathericons.UserCheck
import compose.icons.feathericons.UserX

enum class MemberAction {
    REMOVE,
    PROMOTE,
    DEMOTE
}

@Composable
fun HouseholdScreen(
    modifier: Modifier = Modifier,
    mainUserId: String,
    householdState: HouseholdState,
    onUiEvent: (MainUiEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onEvent: (HouseholdEvent) -> Unit
) {

    var showDialog by rememberSaveable { mutableStateOf<HouseholdUiEvent?>(null) }

    val getAvailableActions = { userId: String, role: String ->
        // TODO: Modify role check
        buildList<MemberAction> {
            if(userId == mainUserId) {
                return@buildList
            } else {
                add(MemberAction.REMOVE)
            }
            if(role != "owner") {
                add(MemberAction.PROMOTE)
            } else {
                add(MemberAction.DEMOTE)
            }
        }.reversed()
    }

    Box(
        modifier = Modifier.then(modifier),
    ) {
        UiStateContainer(
            modifier = Modifier.fillMaxSize(),
            state = householdState.household
        ) { isLoading, household ->

            var householdName by remember {
                mutableStateOf(TextFieldValue(household?.name ?: ""))
            }

            var isEditMode by remember { mutableStateOf(false) }

            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            LaunchedEffect(isEditMode) {
                if (isEditMode) {
                    householdName = householdName.copy(selection = TextRange(householdName.text.length))
                    focusRequester.requestFocus()
                    keyboardController?.show()
                } else {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(72.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = FeatherIcons.Home,
                        contentDescription = null
                    )
                    TextField(
                        modifier = Modifier.focusRequester(focusRequester),
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
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    if(isEditMode) {
                        IconButton(
                            onClick = {
                                // Save household name change
                                isEditMode = false
                                onMainEvent(MainEvent.UpdateHouseholdName(
                                    householdId = household!!.id,
                                    householdName.text
                                ))
                            }
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Check,
                                contentDescription = null
                            )
                        }
                    }
                }
                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp),
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
                            isEditMode = !isEditMode
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
                            // Delete household
                            showDialog = HouseholdUiEvent.DELETE
                        }
                    ) {
                        Text(
                            text = "Delete Household",
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }
                HorizontalDivider()
                // Members
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(256.dp)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Members",
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 24.sp
                            )
                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = {
                                    showDialog = HouseholdUiEvent.EDIT
                                }
                            ) {
                                Icon(
                                    imageVector = FeatherIcons.Plus,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    // TODO: loading status
                    if(!isLoading) {
                        // TODO: null check
                        items(household?.members!!) { member ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RemotePicture(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                MaterialTheme.colorScheme.onSurface.copy(
                                                    0.4f
                                                )
                                            )
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.onSurface,
                                                CircleShape
                                            ),
                                        url = member.picture
                                    )
                                    Text(
                                        text = member.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                // User actions
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (action in getAvailableActions(member.id, member.role)) {
                                        when (action) {
                                            MemberAction.REMOVE -> {
                                                IconButton(
                                                    onClick = {
                                                        onEvent(HouseholdEvent.RemoveMember(member.id))
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = FeatherIcons.UserX,
                                                        contentDescription = null
                                                    )
                                                }
                                            }

                                            MemberAction.DEMOTE -> {
                                                IconButton(
                                                    onClick = {
                                                        onEvent(
                                                            HouseholdEvent.UpdateMemberRole(
                                                                member.id,
                                                                "member"
                                                            )
                                                        )
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = FeatherIcons.ArrowDown,
                                                        contentDescription = null
                                                    )
                                                }
                                            }

                                            MemberAction.PROMOTE -> {
                                                IconButton(
                                                    onClick = {
                                                        onEvent(
                                                            HouseholdEvent.UpdateMemberRole(
                                                                member.id,
                                                                "owner"
                                                            )
                                                        )
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = FeatherIcons.ArrowUp,
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        showDialog?.let { uiEvent ->
            Dialog(
                onDismissRequest = {
                    showDialog = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
                        .padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    when(uiEvent) {
                        HouseholdUiEvent.EDIT -> {
                            var selectedUserIndex by remember { mutableIntStateOf(0) }

                            // Title
                            Text(
                                text = "Add user to household",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            UiStateContainer(
                                modifier = Modifier.fillMaxWidth(),
                                state = householdState.users
                            ) { isLoading, users ->
                                // TODO: Loading status
                                if(!isLoading) {
                                    Column {
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .requiredHeight(256.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            if(users.isNullOrEmpty()) {
                                                item {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = "No users available to add.",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                                                        )
                                                    }
                                                }
                                            } else {
                                                itemsIndexed(users) { index, (id, name) ->
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                selectedUserIndex = index
                                                            }
                                                            .background(
                                                                if (selectedUserIndex == index)
                                                                    MaterialTheme.colorScheme.onSurface.copy(
                                                                        0.15f
                                                                    )
                                                                else Color.Transparent
                                                            )
                                                            .padding(3.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = name
                                                        )
                                                    }
                                                }
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
                                                    showDialog = null
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
                                                    users?.get(selectedUserIndex)?.let {
                                                        onEvent(HouseholdEvent.AddMember(it.first))
                                                        showDialog = null
                                                    }
                                                }
                                            ) {
                                                Text(
                                                    text = "Done"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HouseholdUiEvent.DELETE -> {
                            Column {
                                // Title
                                Text(
                                    text = "Are you sure you want to delete the household?",
                                    style = MaterialTheme.typography.titleLarge
                                )
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
                                            showDialog = null
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
                                            onMainEvent(MainEvent.DeleteHousehold(
                                                householdId = householdState.household.let {
                                                    when(it) {
                                                        is UiState.Success -> it.data!!.id
                                                        else -> ""
                                                    }
                                                }
                                            ))
                                            showDialog = null
                                            onUiEvent(MainUiEvent.NavigateBack)
                                        }
                                    ) {
                                        Text(
                                            text = "Done"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class HouseholdUiEvent {
    EDIT,
    DELETE,
}

@Preview
@Composable
fun HouseholdScreenPreview() {
    LemonTheme {
        Surface {
            HouseholdScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                mainUserId = "1",
                householdState = HouseholdState(
                    household = UiState.Success(
                        data = Household(
                            id = "1",
                            name = "Test Household",
                            members = listOf(
                                Member(
                                    id = "1",
                                    name = "Jane",
                                    role = "Owner",
                                    picture = ""
                                ),
                                Member(
                                    id = "2",
                                    name = "John",
                                    role = "Owner",
                                    picture = ""
                                ),
                                Member(
                                    id = "3",
                                    name = "Jack",
                                    role = "Member",
                                    picture = ""
                                )
                            ),
                        )
                    ),
                    users = UiState.Success(
                        data = listOf(
                            Pair("1", "User 1"),
                            Pair("2", "User 2"),
                        )
                    )
                ),
                onMainEvent = {},
                onUiEvent = {}
            ) {

            }
        }
    }
}