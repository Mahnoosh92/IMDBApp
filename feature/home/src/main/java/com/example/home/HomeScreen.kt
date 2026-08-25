package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.home.components.GenreChipGroup
import com.example.home.components.HeroCarousel
import com.example.home.components.MovieCard
import com.example.home.models.GenreUiModel
import com.example.model.MovieWithGenreItem

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        when (uiState) {
            is HomeUiState.Success -> HomeContent(
                homeUiState = uiState as HomeUiState.Success,
                onGenreSelected = { genre ->
                    homeViewModel.onIntent(HomeIntent.OnSelectGenre(genreId = genre.id))
                },
                onMovieClicked = { movieItem -> },
            )

            is HomeUiState.Loading -> Text(text = "Loading")
            is HomeUiState.Error -> Text(text = (uiState as HomeUiState.Error).message)
        }
    }
}

@Composable
fun HomeContent(homeUiState: HomeUiState.Success, modifier: Modifier = Modifier, onGenreSelected: (GenreUiModel) -> Unit, onMovieClicked: (MovieWithGenreItem) -> Unit) {
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
        nowPlaying(nowPlaying = homeUiState.nowPlaying)
        genres(genres = homeUiState.genres, onGenreSelected = onGenreSelected)
        genreMovies(movies = homeUiState.genreMovies, onMovieClicked = onMovieClicked)
    }
}

private fun LazyStaggeredGridScope.nowPlaying(nowPlaying: List<MovieWithGenreItem>, modifier: Modifier = Modifier) {
    item(key = "now_playing_carousel", span = StaggeredGridItemSpan.FullLine, contentType = "nowPlaying") {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        HeroCarousel(movies = nowPlaying, modifier = modifier.height(screenHeight * 0.3f))
    }
}

private fun LazyStaggeredGridScope.genres(genres: List<GenreUiModel>, modifier: Modifier = Modifier, onGenreSelected: (GenreUiModel) -> Unit) {
    item(key = "genres_section", span = StaggeredGridItemSpan.FullLine, contentType = "nowPlaying") {
        Column(modifier.fillMaxWidth()) {
            Text(text = "Genres", modifier = Modifier.padding(top = 8.dp))
            GenreChipGroup(
                genres = genres,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onGenreSelected = onGenreSelected,
            )
        }
    }
}

private fun LazyStaggeredGridScope.genreMovies(movies: List<MovieWithGenreItem>, modifier: Modifier = Modifier, onMovieClicked: (MovieWithGenreItem) -> Unit) {
    items(movies, key = { "movie_${it.id}" }) { movie ->
        MovieCard(
            movieItem = movie,
            modifier = modifier,
            onMovieClicked = onMovieClicked,
        )
    }
}
