package com.example.imdbapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.designsystem.theme.components.TopAppbar
import com.example.detail.navigation.navigateToDetail
import com.example.home.navigation.navigateToHome
import com.example.imdbapp.navigation.TopLevelDestination
import com.example.model.MovieWithGenreItem
import com.example.profile.navigation.navigateToProfile
import com.example.watchlist.navigation.navigateToWatchList
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(coroutineScope: CoroutineScope = rememberCoroutineScope(), navController: NavHostController = rememberNavController()) = remember(
    navController,
    coroutineScope,
) {
    AppState(
        navController = navController,
        coroutineScope = coroutineScope,
    )
}

@Stable
class AppState(val navController: NavHostController, val coroutineScope: CoroutineScope) {
    val currentDestination: NavDestination?
        @Composable get() =
            navController
                .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) ?: false
            }
        }
    val isTopLevelDestination: Boolean
        @Composable get() = currentTopLevelDestination != null
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions =
            navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(navOptions = topLevelNavOptions)
            TopLevelDestination.WATCHLIST -> navController.navigateToWatchList(navOptions = topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(navOptions = topLevelNavOptions)
        }
    }

    fun navigateToDetails(movieWithGenreItem: MovieWithGenreItem) {
        navController.navigateToDetail(movieWithGenreItem = movieWithGenreItem)
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IMDBApp(appState: AppState, modifier: Modifier = Modifier, windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()) {
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        // TODO: Show settings dialog
    }
    val currentTopLevelDestination = appState.currentTopLevelDestination
    val isTopLevelDestination = appState.isTopLevelDestination
    val layoutType = if (isTopLevelDestination) {
        NavigationSuiteScaffoldDefaults
            .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    } else {
        NavigationSuiteType.None
    }

    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier =
        modifier
            .semantics {
                testTagsAsResourceId = true
            }
            .windowInsetsPadding(WindowInsets.safeDrawing),
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val isSelected = destination == currentTopLevelDestination
                item(
                    selected = isSelected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector =
                            if (isSelected) {
                                destination.selectedIcon
                            } else {
                                destination.unselectedIcon
                            },
                            contentDescription = stringResource(id = destination.iconTextId),
                        )
                    },
                    label = {
                        Text(text = stringResource(id = destination.iconTextId))
                    },
                )
            }
        },
    ) {
        Scaffold(
            modifier =
            modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = LocalSnackbarHostState.current) },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                TopAppbar(
                    titleRes = R.string.app_name,
                    navigationIcon = if (isTopLevelDestination) {
                        Icons.Default.Search
                    } else {
                        Icons.AutoMirrored.Filled.ArrowBack
                    },
                    navigationIconContentDescription = if (isTopLevelDestination) {
                        stringResource(R.string.search)
                    } else {
                        stringResource(R.string.back)
                    },
                    actionIcon = if (isTopLevelDestination) Icons.Default.Settings else null,
                    actionIconContentDescription = stringResource(R.string.setting),
                    colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    onNavigationClick = {
                        if (isTopLevelDestination) {
                            // TODO: Navigate to search screen
                        } else {
                            appState.navController.popBackStack()
                        }
                    },
                    onActionClick = {
                        if (isTopLevelDestination) {
                            showSettingsDialog = true
                        }
                    },
                )
                AppNavHost(
                    appState = appState,
                )
            }
        }
    }
}
