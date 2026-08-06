package com.example.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.profile.ProfileScreen
import kotlinx.serialization.Serializable
@Serializable data object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions) =
    navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen() {
    composable<ProfileRoute>(
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
        ProfileScreen()
    }
}