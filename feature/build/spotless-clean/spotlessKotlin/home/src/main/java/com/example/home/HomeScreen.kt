package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.home.components.HeroCarousel
import com.example.model.MovieItem

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        homeViewModel.onIntent(HomeIntent.GetNowPlayingMovies)
    }
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        when (uiState) {
            is HomeUiState.Success -> HomeContent(nowPlaying = (uiState as HomeUiState.Success).nowPlaying)
            is HomeUiState.Loading -> Text(text = "Loading")
            is HomeUiState.Error -> Text(text = (uiState as HomeUiState.Error).message)
        }
    }
}

@Composable
fun HomeContent(nowPlaying: List<MovieItem>, modifier: Modifier = Modifier) {
    val state = rememberLazyStaggeredGridState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 24.dp,
        modifier = modifier
            .testTag(HomeTags.HOME_MAIN),
        state = state,
    ) {
        nowPlaying(nowPlaying = nowPlaying)
    }
}

private fun LazyStaggeredGridScope.nowPlaying(nowPlaying: List<MovieItem>) {
    item(span = StaggeredGridItemSpan.FullLine, contentType = "nowPlaying") {
        HeroCarousel(movies = nowPlaying)
    }
}
