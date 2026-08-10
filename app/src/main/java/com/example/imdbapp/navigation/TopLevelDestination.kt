package com.example.imdbapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.home.navigation.HomeRoute
import com.example.profile.navigation.ProfileRoute
import com.example.watchlist.navigation.WatchListRoute
import kotlin.reflect.KClass
import com.example.home.R as HomeR
import com.example.profile.R as ProfileR
import com.example.watchlist.R as WatchListR

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
) {
    HOME(
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = HomeR.string.feature_home,
        titleTextId = HomeR.string.feature_home,
        route = HomeRoute::class,
    ),
    WATCHLIST(
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Outlined.Favorite,
        iconTextId = WatchListR.string.feature_watch_list,
        titleTextId = WatchListR.string.feature_watch_list,
        route = WatchListRoute::class,
    ),
    PROFILE(
        selectedIcon = Icons.Default.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = ProfileR.string.feature_profile,
        titleTextId = ProfileR.string.feature_profile,
        route = ProfileRoute::class,
    ),
}
