package com.example.serach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.firebase.crashlytics.CrashReportingRepository
import com.example.firebase.crashlytics.CustomKeys
import com.example.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val crashReportingRepository: CrashReportingRepository,
) : ViewModel() {
    init {
        throw Exception()
    }
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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
            initialValue = SearchUiState.Loading,
        )

    private fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    private fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.OnSearchQueryChanged -> onSearchQueryChanged(intent.query)
            is SearchIntent.OnCreated -> crashReportingRepository.setCustomKey(CustomKeys.USER_SCREEN, "search")
        }
    }
}

sealed interface SearchUiState {
    data class Success(val movies: List<MovieItem>) : SearchUiState
    data object Loading : SearchUiState
    data class Error(val message: String) : SearchUiState
}

sealed interface SearchIntent {
    data class OnSearchQueryChanged(val query: String) : SearchIntent
    data object OnCreated : SearchIntent
}

private const val SEARCH_QUERY = "searchQuery"
