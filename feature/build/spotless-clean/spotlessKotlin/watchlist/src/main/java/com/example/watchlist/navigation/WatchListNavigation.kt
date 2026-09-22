package com.example.watchlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.model.MovieWithGenreItem
import com.example.watchlist.WatchListScreen
import kotlinx.serialization.Serializable

const val WATCHLIST_DEEP_LINK_URI = "https://www.imdb.google.com/watchlist"

@Serializable
data object WatchListRoute

fun NavController.navigateToWatchList(navOptions: NavOptions) = navigate(route = WatchListRoute, navOptions)

fun NavGraphBuilder.watchListScreen(navigateToDetail: (MovieWithGenreItem) -> Unit) {
    composable<WatchListRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = WATCHLIST_DEEP_LINK_URI
            },
        ),
    ) {
        WatchListScreen(navigateToDetail = navigateToDetail)
    }
}
