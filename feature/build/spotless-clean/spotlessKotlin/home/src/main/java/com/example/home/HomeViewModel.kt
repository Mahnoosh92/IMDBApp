package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.home.models.GenreUiModel
import com.example.home.models.toUiModel
import com.example.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    private val selectedGenreId = MutableStateFlow<Int?>(null)

    private val genresResultFlow = flow {
        emit(movieRepository.getGenres(page = 1))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val genreMoviesResultFlow: Flow<Result<List<MovieItem>>> = selectedGenreId
        .filterNotNull()
        .flatMapLatest { genreId ->
            flow {
                emit(Result.success(emptyList<MovieItem>())) // Optional loading state hook
                emit(movieRepository.discoverMovies(genre = genreId.toString()))
            }
        }
    val uiState: StateFlow<HomeUiState> = combine(
        flow { emit(movieRepository.getNowPlayingMovies(page = 1)) },
        genresResultFlow,
        genreMoviesResultFlow.onStart { emit(Result.success(emptyList())) },
    ) { nowPlayingRes, genresRes, genreMoviesRes ->

        // Global Error Handling: If any core request fails, emit Error state
        val nowPlaying = nowPlayingRes.getOrElse { return@combine HomeUiState.Error(it.localizedMessage ?: "Failed to load now playing movies") }
        val genres = genresRes.getOrElse { return@combine HomeUiState.Error(it.localizedMessage ?: "Failed to load genres") }
        val genreMovies = genreMoviesRes.getOrDefault(emptyList())

        if (selectedGenreId.value == null && genres.isNotEmpty()) {
            selectedGenreId.value = genres.first().id
        }

        HomeUiState.Success(
            nowPlaying = nowPlaying,
            genres = genres.map { it.toUiModel(isSelected = it.id == selectedGenreId.value) },
            genreMovies = genreMovies,
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
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val nowPlaying: List<MovieItem>,
        val genres: List<GenreUiModel> = emptyList(),
        val genreMovies: List<MovieItem> = emptyList(),
        val isGenreMoviesLoading: Boolean = false,
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

sealed interface HomeIntent {
    data class OnSelectGenre(val genreId: Int) : HomeIntent
}
