package com.example.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.theme.components.LocalSnackbarHostState
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
                viewModel.onIntent(WatchListIntent.OnWatchlistClicked(movieItem))
            },
            shouldDisplayUndo = viewModel.shouldDisplayUndo,
            onUndoClicked = {
                viewModel.onIntent(WatchListIntent.OnUndoClicked)
            },
        )

        is WatchListUiState.Loading -> Text(text = "Loading")
        is WatchListUiState.Error -> Text(text = (uiState as WatchListUiState.Error).message)
    }
}

@Composable
fun WatchListContent(watchListUiState: WatchListUiState.Success, shouldDisplayUndo: Boolean, modifier: Modifier = Modifier, onMovieClicked: (MovieWithGenreItem) -> Unit, onWatchlistClicked: (MovieWithGenreItem) -> Unit, onUndoClicked: () -> Unit) {
    val snackbarHostState = LocalSnackbarHostState.current
    val bookmarkRemovedMessage = stringResource(id = R.string.feature_bookmarks_removed)
    val undoText = stringResource(id = R.string.feature_bookmarks_undo)
    LaunchedEffect(shouldDisplayUndo) {
        if (shouldDisplayUndo) {
            val result = snackbarHostState.showSnackbar(
                message = bookmarkRemovedMessage,
                actionLabel = undoText,
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) {
                onUndoClicked()
            }
        }
    }
    val state = rememberLazyStaggeredGridState()
    if (watchListUiState.movies.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 24.dp,
            modifier = modifier
                .fillMaxSize()
                .testTag(WatchListTag.WATCH_LIST_MAIN),
            state = state,
        ) {
            genreMovies(movies = watchListUiState.movies, onMovieClicked = onMovieClicked, onWatchlistClicked = onWatchlistClicked)
        }
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

@Composable
fun EmptyList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.feature_watchlist_empty_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.feature_watchlist_empty_desc),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
