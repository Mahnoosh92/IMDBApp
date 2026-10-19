package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.UserRepository
import com.example.model.DarkThemeConfig
import com.example.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val uiState = userRepository.userData
        .map<UserData, ProfileUiState> { userData ->
            ProfileUiState.Success(darkThemeConfig = userData.darkThemeConfig, watchListCount = userData.watchListMovies.size)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading,
        )

    fun setDarkThemeConfig(config: DarkThemeConfig) {
        viewModelScope.launch {
            userRepository.setDarkThemeConfig(config)
        }
    }
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val darkThemeConfig: DarkThemeConfig, val watchListCount: Int = 0) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
