package com.srnyndrs.android.lemon.ui.screen.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto
import com.srnyndrs.android.lemon.ui.components.forms.TransactionForm
import com.srnyndrs.android.lemon.ui.screen.main.content.category.CategoryScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.category.CategoryViewModel
import com.srnyndrs.android.lemon.ui.screen.main.content.home.HomeEvent
import com.srnyndrs.android.lemon.ui.screen.main.content.home.HomeScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.home.HomeViewModel
import com.srnyndrs.android.lemon.ui.screen.main.content.household.HouseholdScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.household.HouseholdViewModel
import com.srnyndrs.android.lemon.ui.screen.main.content.insights.InsightsScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.insights.InsightsViewModel
import com.srnyndrs.android.lemon.ui.screen.main.content.profile.ProfileScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.transactions.TransactionsScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.transactions.TransactionsViewModel
import com.srnyndrs.android.lemon.ui.screen.main.content.transactions.editor.TransactionEditorScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.transactions.editor.TransactionEditorViewModel
import com.srnyndrs.android.lemon.ui.screen.main.content.wallet.WalletScreen
import com.srnyndrs.android.lemon.ui.screen.main.content.wallet.WalletViewModel
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.User
import compose.icons.feathericons.X
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainState: MainState,
    onMainEvent: (MainEvent) -> Unit,
) {

    var privacyMode by rememberSaveable { mutableStateOf(true) }
    var selectedMenuItem by rememberSaveable { mutableIntStateOf(0) }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val screens = listOf(
        Screens.Home,
        Screens.Insights,
        Screens.Wallet,
        Screens.Categories,
    )

    val topPadding = 96.dp

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.let { currentRoute ->
            screens.find { it.route == currentRoute }?.let { screen ->
                selectedMenuItem = screens.indexOf(screen)
            }
        }
    }

    Scaffold(
        modifier = Modifier.then(modifier),
            //.background(MaterialTheme.colorScheme.tertiary),
        topBar = {

            val visible = screens.map { it.route }.contains(navBackStackEntry?.destination?.route)

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                visible = visible,
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
                                        navController.navigate(Screens.Profile.route)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(72.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal)
            ) {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = index == selectedMenuItem,
                        onClick = {
                            navController.navigate(screen.route)
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = screen.icon!!,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
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
        if (mainState.error == null) {
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                startDestination = Screens.Home.route
            ) {
                composable(
                    route = Screens.Home.route
                ) {

                    val homeViewModel = hiltViewModel<HomeViewModel, HomeViewModel.HomeViewModelFactory>(
                        creationCallback = { factory ->
                            factory.create(mainState.selectedHouseholdId)
                        }
                    )

                    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

                    LaunchedEffect(mainState.selectedHouseholdId) {
                        if (mainState.selectedHouseholdId.isNotBlank()) {
                            homeViewModel.onEvent(HomeEvent.SwitchHousehold(mainState.selectedHouseholdId))
                        }
                    }

                    HomeScreen(
                        modifier = Modifier.fillMaxSize().padding(top = topPadding),
                        homeState = homeState,
                        households = mainState.user.households,
                        selectedHouseholdId = mainState.selectedHouseholdId,
                        isLoading = mainState.isLoading,
                        onHomeEvent = { event ->
                            homeViewModel.onEvent(event)
                        },
                        onUiEvent = { event ->
                            when(event) {
                                is MainUiEvent.ShowBottomSheet -> {
                                    navController.navigate(Screens.TransactionEditor.route)
                                }
                                is MainUiEvent.ShowTransactions -> {
                                    navController.navigate(Screens.Transactions.route)
                                }
                                is MainUiEvent.ShowHousehold -> {
                                    navController.navigate(Screens.Household.route)
                                }
                                is MainUiEvent.ShowTransactionEditor -> {
                                    val path = buildString {
                                        append(Screens.TransactionEditor.route)
                                        event.transactionId?.let {
                                            append("?transactionId=${it}")
                                        }
                                    }
                                    navController.navigate(path)
                                }
                                is MainUiEvent.NavigateBack -> {
                                    navController.popBackStack()
                                }
                            }
                        }
                    ) { mainEvent ->
                        onMainEvent(mainEvent)
                    }
                }
                composable(
                    route = Screens.Insights.route
                ) {

                    val insightsViewModel = hiltViewModel<InsightsViewModel, InsightsViewModel.InsightsViewModelFactory>(
                        creationCallback = { factory ->
                            factory.create(mainState.selectedHouseholdId)
                        }
                    )

                    val insightsState by insightsViewModel.insightsState.collectAsStateWithLifecycle()

                    InsightsScreen(
                        modifier = Modifier.fillMaxSize().padding(top = topPadding),
                        insightsState = insightsState
                    )
                }
                composable(
                    route = Screens.Wallet.route
                ) {

                    val walletViewModel = hiltViewModel<WalletViewModel, WalletViewModel.WalletViewModelFactory>(
                        creationCallback = { factory ->
                            factory.create(
                                mainState.selectedHouseholdId,
                                mainState.user.userId
                            )
                        }
                    )

                    val walletState by walletViewModel.walletState.collectAsStateWithLifecycle()

                    WalletScreen(
                        modifier = Modifier.fillMaxSize().padding(top = topPadding),
                        state = walletState,
                        onEvent = { event ->
                            walletViewModel.onEvent(event)
                        }
                    )
                }
                composable(
                    route = Screens.Categories.route
                ) {

                    val categoryViewModel = hiltViewModel<CategoryViewModel, CategoryViewModel.CategoryViewModelFactory>(
                        creationCallback = { factory -> factory.create(mainState.selectedHouseholdId) }
                    )

                    val categoriesState by categoryViewModel.categoryState.collectAsStateWithLifecycle()

                    CategoryScreen(
                        modifier = Modifier.fillMaxSize().padding(top = topPadding),
                        categoriesState = categoriesState,
                        onEvent = { categoryEvent ->
                            categoryViewModel.onEvent(categoryEvent)
                        }
                    )
                }
                composable(
                    route = Screens.Transactions.route,
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    }
                ) {

                    val transactionsViewModel = hiltViewModel<TransactionsViewModel, TransactionsViewModel.TransactionsViewModelFactory>(
                        creationCallback = { factory -> factory.create(mainState.selectedHouseholdId) }
                    )

                    val transactionState by transactionsViewModel.transactionState.collectAsStateWithLifecycle()

                    TransactionsScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        transactionState = transactionState
                    ) { event ->
                        transactionsViewModel.onEvent(event)
                    }
                }
                composable(
                    route = Screens.Profile.route,
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    }
                ) {
                    ProfileScreen(
                        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                        username = mainState.user.username,
                        email = mainState.user.email,
                        onMainEvent = {
                            onMainEvent(it)
                        },
                    )
                }
                composable(
                    route = Screens.Household.route,
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    }
                ) {

                    val householdViewModel = hiltViewModel<HouseholdViewModel, HouseholdViewModel.HouseholdViewModelFactory>(
                        creationCallback = { factory -> factory.create(
                            householdId = mainState.selectedHouseholdId,
                            userId = mainState.user.userId
                        )}
                    )

                    val householdState by householdViewModel.uiState.collectAsStateWithLifecycle()

                    HouseholdScreen(
                        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                        mainUserId = mainState.user.userId,
                        householdState = householdState,
                        onUiEvent = { uiEvent ->
                            // TODO: generic handler function for ui events
                            when (uiEvent) {
                                is MainUiEvent.NavigateBack -> {
                                    navController.popBackStack()
                                }
                                else -> {}
                            }
                        },
                        onMainEvent = {
                            onMainEvent(it)
                        }
                    ) { event ->
                        householdViewModel.onEvent(event)
                    }
                }
                composable(
                    route = "${Screens.TransactionEditor.route}?transactionId={transactionId}",
                    arguments = listOf(
                        navArgument("transactionId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    ),
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400)
                        )
                    }
                ) { backStackEntry ->

                    val transactionId = backStackEntry.arguments?.getString("transactionId")

                    val transactionEditorViewModel = hiltViewModel<TransactionEditorViewModel, TransactionEditorViewModel.TransactionEditorViewModelFactory>(
                        creationCallback = { factory ->
                            factory.create(
                                mainState.selectedHouseholdId,
                                mainState.user.userId,
                                transactionId
                            )
                        }
                    )

                    val transactionEditorState by transactionEditorViewModel.uiState.collectAsStateWithLifecycle()

                    TransactionEditorScreen(
                        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                        state = transactionEditorState,
                        onBack = {
                            // TODO
                            navController.navigate(Screens.Home.route)
                        }
                    ) { event ->
                        transactionEditorViewModel.onEvent(event)
                    }
                }
            }
        } else {
            // TODO: Show error screen
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error: ${mainState.error}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
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
                mainState = MainState(),
                onMainEvent = {}
            )
        }
    }
}