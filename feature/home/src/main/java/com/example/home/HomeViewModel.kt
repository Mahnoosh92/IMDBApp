package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.home.models.GenreUiModel
import com.example.home.models.toUiModel
import com.example.model.MovieItem
import com.example.model.MovieWithGenreItem
import com.example.model.toMovieItem
import com.example.model.toMovieWithGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    private val selectedGenreId = MutableStateFlow<Int?>(null)

    private val genresResultFlow = flow {
        emit(movieRepository.getGenres(page = 1))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val genreMoviesResultFlow: Flow<Result<List<MovieItem>>> = selectedGenreId
        .filterNotNull()
        .flatMapLatest { genreId ->
            flow {
                emit(movieRepository.discoverMovies(genre = genreId.toString()))
            }
        }
    val uiState: StateFlow<HomeUiState> = combine(
        movieRepository.userData,
        flow { emit(movieRepository.getNowPlayingMovies(page = 1)) },
        genresResultFlow,
        genreMoviesResultFlow.onStart { emit(Result.success(emptyList())) },
    ) { userData, nowPlayingRes, genresRes, genreMoviesRes ->

        // Global Error Handling: If any core request fails, emit Error state
        val nowPlaying = nowPlayingRes.getOrElse { return@combine HomeUiState.Error(it.localizedMessage ?: "Failed to load now playing movies") }
        val genres = genresRes.getOrElse { return@combine HomeUiState.Error(it.localizedMessage ?: "Failed to load genres") }
        val genreMovies = genreMoviesRes.getOrDefault(emptyList())

        if (selectedGenreId.value == null && genres.isNotEmpty()) {
            selectedGenreId.value = genres.first().id
        }

        val watchListIds = userData.watchListMovies.map(MovieItem::id).toSet()
        val genreLookupMap = genres.associateBy { it.id }
        val nowPlayingUiModels = nowPlaying.map { it.toMovieWithGenre(genreLookupMap = genreLookupMap, isWatchListed = it.id in watchListIds) }
        val genreMoviesUiModels = genreMovies.map { it.toMovieWithGenre(genreLookupMap = genreLookupMap, isWatchListed = it.id in watchListIds) }

        HomeUiState.Success(
            nowPlaying = nowPlayingUiModels,
            genres = genres.map { it.toUiModel(isSelected = it.id == selectedGenreId.value) },
            genreMovies = genreMoviesUiModels,
            isGenreMoviesLoading = genreMoviesRes.isFailure,
        )
    }
        .catch { throwable ->
            emit(HomeUiState.Error(throwable.localizedMessage ?: "An unexpected error occurred"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading,
        )

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnSelectGenre -> {
                selectedGenreId.value = intent.genreId
            }

            is HomeIntent.OnWatchlistClicked -> toggleWatchlist(movie = intent.movieWithGenreItem.toMovieItem())
        }
    }

    private fun toggleWatchlist(movie: MovieItem) {
        viewModelScope.launch {
            val currentWatchlist = (movieRepository.userData.firstOrNull()?.watchListMovies ?: emptyList())
            val isAlreadyWatchListed = currentWatchlist.any { it.id == movie.id }

            if (isAlreadyWatchListed) {
                movieRepository.removeWatchItem(movie)
            } else {
                movieRepository.addWatchItem(movie)
            }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val nowPlaying: List<MovieWithGenreItem>,
        val genres: List<GenreUiModel> = emptyList(),
        val genreMovies: List<MovieWithGenreItem> = emptyList(),
        val isGenreMoviesLoading: Boolean = false,
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

sealed interface HomeIntent {
    data class OnSelectGenre(val genreId: Int) : HomeIntent
    data class OnWatchlistClicked(val movieWithGenreItem: MovieWithGenreItem) : HomeIntent
}
