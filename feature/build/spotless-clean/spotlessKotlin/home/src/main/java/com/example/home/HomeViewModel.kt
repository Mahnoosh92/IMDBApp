package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetNowPlayingMovies -> getNowPlayingMovies()
            else -> {}
        }
    }
    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = movieRepository.getNowPlayingMovies(page = 1).fold(
                onSuccess = { movies -> HomeUiState.Success(movies) },
                onFailure = { throwable ->
                    HomeUiState.Error(throwable.localizedMessage ?: "Unknown Error")
                },
            )
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val nowPlaying: List<MovieItem>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface HomeIntent {
    data object GetNowPlayingMovies : HomeIntent
}
