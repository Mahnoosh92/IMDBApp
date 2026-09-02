package com.example.imdbapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.detail.navigation.detailScreen
import com.example.home.navigation.HomeRoute
import com.example.home.navigation.homeScreen
import com.example.profile.navigation.profileScreen
import com.example.watchlist.navigation.watchListScreen

@Composable
fun AppNavHost(appState: AppState, modifier: Modifier = Modifier) {
    NavHost(
        navController = appState.navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeScreen(navigateToDetail = appState::navigateToDetails)
        watchListScreen()
        profileScreen()
        detailScreen()
    }
}
