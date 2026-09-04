package com.example.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.model.MovieWithGenreItem
import com.example.model.toMovieWithGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
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
}

sealed interface WatchListUiState {
    data object Loading : WatchListUiState
    data class Success(val movies: List<MovieWithGenreItem>) : WatchListUiState
    data class Error(val message: String) : WatchListUiState
}
