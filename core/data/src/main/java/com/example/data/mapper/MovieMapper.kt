package com.example.data.mapper

import com.example.model.Genre
import com.example.model.MovieItem
import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto

fun MovieItemDto.toDomain(): MovieItem = MovieItem(
    id = id,
    mediaType = mediaType,
    adult = adult,
    backdropPath = backdropPath,
    posterPath = posterPath,
    overview = overview,
    originalLanguage = originalLanguage,
    genreIds = genreIds,
    popularity = popularity,
    voteAverage = voteAverage,
    voteCount = voteCount,
    title = title,
    originalTitle = originalTitle,
    releaseDate = releaseDate,
    video = video,
    name = name,
    originalName = originalName,
    firstAirDate = firstAirDate,
    originCountry = originCountry ?: emptyList(),
)

fun GenreDTO.toDomain(): Genre = Genre(
    id = this.id,
    name = this.name,
)
