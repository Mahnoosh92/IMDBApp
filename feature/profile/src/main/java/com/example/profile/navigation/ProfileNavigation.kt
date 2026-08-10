package com.example.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.profile.ProfileScreen
import kotlinx.serialization.Serializable
@Serializable data object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen() {
    composable<ProfileRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = ""
            },
        ),
    ) {
        ProfileScreen()
    }
}
