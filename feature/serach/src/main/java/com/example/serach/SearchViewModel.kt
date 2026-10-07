package com.example.serach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository
) : ViewModel() {
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    val uiState: StateFlow<SearchUiState> = searchQuery.debounce(500).flatMapLatest { query ->
        flow {
            emit(movieRepository.searchMovies(query = query, includeAdult = false))
        }
    }
        .map { result ->
            result.fold(onSuccess = { SearchUiState.Success(it) }, onFailure = { throwable -> SearchUiState.Error(throwable.message ?: "UnExpected Error") })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState.Loading
        )


    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

}


sealed interface SearchUiState {
    data class Success(val movies: List<MovieItem>) : SearchUiState
    data object Loading : SearchUiState
    data class Error(val message: String) : SearchUiState
}

private const val SEARCH_QUERY = "searchQuery"
