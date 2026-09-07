package com.example.watchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.model.MovieItem
import com.example.model.MovieWithGenreItem
import com.example.model.toMovieItem
import com.example.model.toMovieWithGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    var shouldDisplayUndo by mutableStateOf(false)
    private var lastRemovedMovie: MovieItem? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val watchListUiState: StateFlow<WatchListUiState> = flow {
        val genresResult = movieRepository.getGenres(page = 1)
        genresResult.fold(onSuccess = { genres ->
            val genreMap = genres.associateBy { it.id }
            emit(genreMap)
        }, onFailure = {
            emit(emptyMap())
        })
    }.flatMapLatest { genreMap ->
        movieRepository.userData.map { userData ->
            userData.watchListMovies.map { movieItem ->
                movieItem.toMovieWithGenre(
                    genreLookupMap = genreMap,
                    isWatchListed = true,
                )
            }
        }.map<List<MovieWithGenreItem>, WatchListUiState>(WatchListUiState::Success)
    }.catch { throwable ->
        emit(WatchListUiState.Error(throwable.localizedMessage ?: "An unexpected error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WatchListUiState.Loading,
    )

    fun onIntent(intent: WatchListIntent) {
        when (intent) {
            is WatchListIntent.OnWatchlistClicked -> toggleWatchlist(movie = intent.movieWithGenreItem.toMovieItem())
            is WatchListIntent.OnUndoClicked -> undoRemoveFromWatchlist()
        }
    }

    private fun toggleWatchlist(movie: MovieItem) {
        viewModelScope.launch {
            val currentWatchlist = (movieRepository.userData.firstOrNull()?.watchListMovies ?: emptyList())
            val isAlreadyWatchListed = currentWatchlist.any { it.id == movie.id }

            if (isAlreadyWatchListed) {
                shouldDisplayUndo = true
                lastRemovedMovie = movie
                movieRepository.removeWatchItem(movie)
            } else {
                shouldDisplayUndo = false
                lastRemovedMovie = null
                movieRepository.addWatchItem(movie)
            }
        }
    }
    private fun undoRemoveFromWatchlist() {
        viewModelScope.launch {
            lastRemovedMovie?.let { movie ->
                movieRepository.addWatchItem(movie)
                lastRemovedMovie = null
                shouldDisplayUndo = false
            }
        }
    }
}
sealed interface WatchListUiState {
    data object Loading : WatchListUiState
    data class Success(val movies: List<MovieWithGenreItem>) : WatchListUiState
    data class Error(val message: String) : WatchListUiState
}
sealed interface WatchListIntent {
    data class OnWatchlistClicked(val movieWithGenreItem: MovieWithGenreItem) : WatchListIntent
    data object OnUndoClicked : WatchListIntent
}
