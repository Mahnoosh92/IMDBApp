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
                uriPattern = ""
            },
        ),
    ) {
        HomeScreen()
    }
}
