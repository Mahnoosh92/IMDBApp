package com.example.serach

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen(modifier: Modifier = Modifier, searchViewModel: SearchViewModel = hiltViewModel()) {
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is SearchUiState.Success -> Text(text = "Success")
        is SearchUiState.Loading -> Text(text = "Loading")
        is SearchUiState.Error -> Text(text = (uiState as SearchUiState.Error).message)
    }

}
