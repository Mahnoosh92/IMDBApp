package com.example.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.home.models.GenreUiModel

@Composable
fun GenreChipGroup(genres: List<GenreUiModel>, onGenreSelected: (GenreUiModel) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        state = rememberLazyListState(),
    ) {
        items(
            items = genres,
            key = { it.id },
        ) { genre ->
            FilterChip(
                selected = genre.isSelected,
                onClick = { onGenreSelected(genre) },
                label = { Text(text = genre.name) },
            )
        }
    }
}
