package com.srnyndrs.android.lemon.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.ui.components.forms.TransactionForm
import com.srnyndrs.android.lemon.ui.screen.main.content.wallet.WalletScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.home.HomeScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.profile.ProfileScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.transactions.TransactionsScreen
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.User
import compose.icons.feathericons.VolumeX
import compose.icons.feathericons.X
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainState: MainState,
    onMainEvent: (MainEvent<*>) -> Unit,
) {

    val scope = rememberCoroutineScope()
    var privacyMode by rememberSaveable { mutableStateOf(true) }
    var selectedMenuItem by rememberSaveable { mutableIntStateOf(0) }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val screens = listOf(
        Screens.Home,
        Screens.Wallet,
        Screens.Transactions,
        Screens.Profile
    )

    val topPadding = 96.dp

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            confirmValueChange = {
                //it != SheetValue.Expanded
                it != SheetValue.PartiallyExpanded
            },
            skipHiddenState = false
        )
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
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = bottomSheetState.bottomSheetState.targetValue != SheetValue.Expanded,
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(topPadding)
                        .clip(RoundedCornerShape(
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        ))
                        .background( MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    .clickable {
                                        // TODO: Open profile settings

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
                            Text(text = mainState.user.username)
                        }
                        IconButton(
                            modifier = Modifier.size(24.dp),
                            onClick = {
                                privacyMode = !privacyMode
                                // TODO: authentication
                            }
                        ) {
                            Icon(
                                imageVector = if (privacyMode) FeatherIcons.EyeOff else FeatherIcons.Eye,
                                contentDescription = null
                            )
                        }
                    }
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
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(bottom = paddingValues.calculateBottomPadding()),
            scaffoldState = bottomSheetState,
            sheetDragHandle = {},
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, bottom = paddingValues.calculateBottomPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add Transaction",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        IconButton(
                            onClick = {
                                scope.launch {
                                    bottomSheetState.bottomSheetState.hide()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = FeatherIcons.X,
                                contentDescription = null
                            )
                        }
                    }
                    TransactionForm(
                        modifier = Modifier.fillMaxSize(),
                        categories = mainState.categories,
                        payments = emptyList()
                    ) { transaction ->
                        //onMainEvent(MainEvent.AddTransaction(transaction))
                    }
                }
            },
            //sheetPeekHeight = 372.dp,
            sheetSwipeEnabled = false
        ) {
            if (!mainState.isLoading) {
                if (mainState.error == null) {
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = Screens.Home.route
                    ) {
                        composable(route = Screens.Home.route) {
                            HomeScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = topPadding - 18.dp),
                                households = mainState.user.households,
                                selectedHouseholdId = mainState.selectedHouseholdId,
                                onUiEvent = {
                                    scope.launch {
                                        //bottomSheetState.bottomSheetState.partialExpand()
                                        bottomSheetState.bottomSheetState.expand()
                                    }
                                }
                            ) { mainEvent ->
                                onMainEvent(mainEvent)
                            }
                        }
                        composable(route = Screens.Wallet.route) {
                            WalletScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = topPadding),
                                categories = mainState.categories,
                                payments = mainState.paymentMethods,
                                onAddPaymentMethod = { paymentMethod ->
                                    onMainEvent(MainEvent.AddPaymentMethod(paymentMethod))
                                }
                            ) { category ->
                                onMainEvent(MainEvent.AddCategory(category))
                            }
                        }
                        composable(route = Screens.Transactions.route) {
                            TransactionsScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = topPadding)
                            )
                        }
                        composable(route = Screens.Profile.route) {
                            ProfileScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = topPadding),
                                onLogout = {
                                    onMainEvent(MainEvent.Logout)
                                }
                            )
                        }
                    }
                } else {
                    // TODO: Show error screen
                    Text(text = "Error: ${mainState.error}")
                }
            } else {
                // TODO: user shimmer effect instead
                CircularProgressIndicator()
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
                mainState = MainState(),
                onMainEvent = {}
            )
        }
    }
}