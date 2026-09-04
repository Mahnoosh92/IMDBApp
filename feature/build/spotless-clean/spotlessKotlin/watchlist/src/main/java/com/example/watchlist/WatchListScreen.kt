package com.example.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.theme.components.MovieCard
import com.example.model.MovieWithGenreItem

@Composable
fun WatchListScreen(modifier: Modifier = Modifier, viewModel: WatchListViewModel = hiltViewModel(), navigateToDetail: (MovieWithGenreItem) -> Unit) {
    val uiState by viewModel.watchListUiState.collectAsStateWithLifecycle()
    when (uiState) {
        is WatchListUiState.Success -> WatchListContent(
            watchListUiState = uiState as WatchListUiState.Success,
            onMovieClicked = navigateToDetail,
            onWatchlistClicked = { movieItem ->
            },
        )

        is WatchListUiState.Loading -> Text(text = "Loading")
        is WatchListUiState.Error -> Text(text = (uiState as WatchListUiState.Error).message)
    }
}

@Composable
fun WatchListContent(watchListUiState: WatchListUiState.Success, modifier: Modifier = Modifier, onMovieClicked: (MovieWithGenreItem) -> Unit, onWatchlistClicked: (MovieWithGenreItem) -> Unit) {
    val state = rememberLazyStaggeredGridState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 24.dp,
        modifier = modifier
            .testTag(WatchListTag.WATCH_LIST_MAIN),
        state = state,
    ) {
        genreMovies(movies = watchListUiState.movies, onMovieClicked = onMovieClicked, onWatchlistClicked = onWatchlistClicked)
    }
}

private fun LazyStaggeredGridScope.genreMovies(movies: List<MovieWithGenreItem>, modifier: Modifier = Modifier, onMovieClicked: (MovieWithGenreItem) -> Unit, onWatchlistClicked: (MovieWithGenreItem) -> Unit) {
    items(movies, key = { "movie_${it.id}" }) { movie ->
        MovieCard(
            movieItem = movie,
            modifier = modifier,
            onMovieClicked = onMovieClicked,
            onWatchlistClicked = onWatchlistClicked,
        )
    }
}
