package com.srnyndrs.android.lemon.ui.screen.main

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.domain.database.model.UserMainData
import com.srnyndrs.android.lemon.ui.screen.main.content.categories.CategoriesScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.home.HomeScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.profile.ProfileScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.wallet.WalletScreen
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import com.srnyndrs.android.lemon.ui.utils.UiState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.User

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    user: UiState<UserMainData>,
    onMainEvent: (MainEvent<*>) -> Unit,
    email: String?,
) {

    val color = remember { Animatable(Color.Gray) }

    LaunchedEffect(Unit) {
        while(true) {
            color.animateTo(Color.Red, animationSpec = tween(10000))
            color.animateTo(Color.Green, animationSpec = tween(10000))
            color.animateTo(Color.Blue, animationSpec = tween(10000))
            color.animateTo(Color.Yellow, animationSpec = tween(10000))
            color.animateTo(Color.Magenta, animationSpec = tween(10000))
            color.animateTo(Color.Cyan, animationSpec = tween(10000))
        }
    }

    var privacyMode by rememberSaveable { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }
    var selectedMenuItem by rememberSaveable { mutableIntStateOf(0) }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val screens = listOf(
        Screens.Home,
        Screens.Wallet,
        Screens.Categories,
        Screens.Profile
    )

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.let { currentRoute ->
            screens.find { it.route == currentRoute }?.let { screen ->
                selectedMenuItem = screens.indexOf(screen)
            }
        }
    }

    Scaffold(
        modifier = Modifier.then(modifier),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
                    /*.background(Brush.horizontalGradient(
                        colors = listOf(
                            Color.Yellow.copy(1f),
                            Color.Yellow.copy(0.5f),
                            Color.Yellow.copy(0.3f),
                        )
                    ))*/
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            .clickable(
                                enabled = user is UiState.Success
                            ) {
                                // TODO: Open profile settings
                                isExpanded = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder for profile image
                        Icon(
                            imageVector = FeatherIcons.User,
                            contentDescription = null,
                        )
                    }
                    //
                    if(user is UiState.Success) {
                        Text(text = user.data.username)
                    } else {
                        Text(text = "household" )
                    }
                    //
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        val households = when(user) {
                            is UiState.Success -> user.data.households
                            else -> emptyList()
                        }
                        households.forEach { household ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = household.name)
                                },
                                onClick = {
                                    // TODO: navigate to household
                                }
                            )
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        privacyMode = !privacyMode
                        // TODO: authentication
                    }
                ) {
                    Icon(
                        imageVector = if(privacyMode) FeatherIcons.EyeOff else FeatherIcons.Eye,
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth().requiredHeight(72.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal)
            ) {
                repeat(4) {
                    NavigationBarItem(
                        selected = it == selectedMenuItem,
                        onClick = {
                            //selectedMenuItem = it
                            navController.navigate(screens[it].route)
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = screens[it].icon!!,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = screens[it].title,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = Screens.Home.route
        ) {
            composable(route = Screens.Home.route) {
                HomeScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screens.Wallet.route) {
                WalletScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = Screens.Categories.route) {
                CategoriesScreen(
                    modifier = Modifier.fillMaxSize(),
                    categories = emptyList()
                )
            }
            composable(route = Screens.Profile.route) {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    onLogout = {
                        onMainEvent(MainEvent.Logout)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    LemonTheme {
        Surface {
            MainScreen(
                modifier = Modifier.fillMaxSize(),
                user = UiState.Empty(),
                email = "test@test.com",
                onMainEvent = {}
            )
        }
    }
}