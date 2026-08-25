package com.example.model

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

fun MovieItem.toMovieWithGenre(genreLookupMap: Map<Int, Genre>): MovieWithGenreItem {
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
        voteAverage = voteAverage,
        voteCount = voteCount,
        originalTitle = originalTitle,
        releaseDate = releaseDate,
        video = video,
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate,
        originCountry = originCountry,
    )
}
