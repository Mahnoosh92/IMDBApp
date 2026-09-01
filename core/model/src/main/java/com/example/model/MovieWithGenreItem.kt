package com.example.model

data class MovieWithGenreItem(
    val id: Int,
    val mediaType: String,
    val adult: Boolean,
    val backdropPath: String? = null,
    val posterPath: String? = null,
    val overview: String,
    val originalLanguage: String,
    val genreIds: List<Genre>,
    val popularity: Double,
    val voteAverage: String,
    val voteCount: String,
    val title: String,
    val originalTitle: String,
    val releaseDate: String,
    val video: Boolean,
    val name: String,
    val originalName: String,
    val firstAirDate: String,
    val originCountry: List<String> = emptyList(),
)
