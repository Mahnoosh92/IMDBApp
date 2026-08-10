package com.example.watchlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.watchlist.WatchListScreen
import kotlinx.serialization.Serializable

@Serializable
data object WatchListRoute

fun NavController.navigateToWatchList(navOptions: NavOptions) = navigate(route = WatchListRoute, navOptions)

fun NavGraphBuilder.watchListScreen() {
    composable<WatchListRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = ""
            },
        ),
    ) {
        WatchListScreen()
    }
}
