package com.example.model

data class MediaItem(
    val id: Int,
    val mediaType: String,
    val adult: Boolean,
    val backdropPath: String? = null,
    val posterPath: String? = null,
    val overview: String,
    val originalLanguage: String,
    val genreIds: List<Int>,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    val title: String? = null,
    val originalTitle: String? = null,
    val releaseDate: String? = null,
    val video: Boolean? = null,
    val name: String? = null,
    val originalName: String? = null,
    val firstAirDate: String? = null,
    val originCountry: List<String> = emptyList(),
)
