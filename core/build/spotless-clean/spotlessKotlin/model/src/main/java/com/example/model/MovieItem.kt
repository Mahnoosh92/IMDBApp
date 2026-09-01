package com.example.model

import java.util.Locale

data class MovieItem(
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
    val title: String,
    val originalTitle: String,
    val releaseDate: String,
    val video: Boolean,
    val name: String,
    val originalName: String,
    val firstAirDate: String,
    val originCountry: List<String> = emptyList(),
)

fun MovieItem.toMovieWithGenre(genreLookupMap: Map<Int, Genre>, isWatchListed: Boolean = false): MovieWithGenreItem {
    return MovieWithGenreItem(
        id = id,
        title = title.ifEmpty { name },
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        mediaType = mediaType,
        adult = adult,
        originalLanguage = originalLanguage,
        genreIds = genreIds.mapNotNull { id -> genreLookupMap[id] },
        popularity = popularity,
        voteAverageString = String.format(Locale.getDefault(), "%.1f", voteAverage),
        voteCountString = "(${formatVoteCount(voteCount)})",
        voteAverage = voteAverage,
        voteCount = voteCount,
        originalTitle = originalTitle,
        releaseDate = releaseDate,
        video = video,
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate,
        originCountry = originCountry,
        isWatchListed = isWatchListed,
    )
}

private fun formatVoteCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format(Locale.getDefault(), "%.1fk", count / 1_000.0)
        else -> count.toString()
    }
}
