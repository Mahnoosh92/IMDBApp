package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen() {
    composable<HomeRoute>(
        deepLinks = listOf(
            navDeepLink {
                /**
                 * This destination has a deep link that enables a specific news resource to be
                 * opened from a notification (@see SystemTrayNotifier for more). The news resource
                 * ID is sent in the URI rather than being modelled in the route type because it's
                 * transient data (stored in SavedStateHandle) that is cleared after the user has
                 * opened the news resource.
                 */
                uriPattern = ""
            },
        ),
    ) {
        HomeScreen()
    }
}

