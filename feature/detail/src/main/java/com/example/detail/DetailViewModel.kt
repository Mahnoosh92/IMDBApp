package com.example.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.detail.navigation.DetailRoute
import com.example.detail.navigation.MovieWithGenreItemNavType
import com.example.model.MovieWithGenreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route: DetailRoute = savedStateHandle.toRoute<DetailRoute>(
        typeMap = mapOf(typeOf<MovieWithGenreItem>() to MovieWithGenreItemNavType),
    )

    private val _uiState = MutableStateFlow<DetailUiState>(
        DetailUiState.Success(route.movieWithGenreItem),
    )
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
}

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val movie: MovieWithGenreItem) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
