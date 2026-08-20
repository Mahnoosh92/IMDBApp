package com.example.home.models

import com.example.model.Genre

data class GenreUiModel(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false,
)

internal fun Genre.toUiModel(isSelected: Boolean = false) = GenreUiModel(
    id = id,
    name = name,
    isSelected = isSelected,
)
