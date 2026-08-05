package com.example.imdbapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datastore.IMDBPreferencesDataSource
import com.example.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val preferencesDataSource: IMDBPreferencesDataSource) :
    ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =
        preferencesDataSource.userData.map<UserData, MainActivityUiState> {
            MainActivityUiState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = MainActivityUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}