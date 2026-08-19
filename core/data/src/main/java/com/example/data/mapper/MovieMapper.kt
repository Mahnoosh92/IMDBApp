package com.example.data.mapper

import com.example.model.Genre
import com.example.model.MovieItem
import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
fun MovieItemDto.toDomain(): MovieItem = MovieItem(
    id = id ?: 0,
    mediaType = mediaType ?: "",
    adult = adult ?: false,
    backdropPath = BASE_IMAGE_URL + backdropPath,
    posterPath = BASE_IMAGE_URL + posterPath,
    overview = overview ?: "",
    originalLanguage = originalLanguage ?: "",
    genreIds = genreIds ?: emptyList(),
    popularity = popularity ?: 0.0,
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    title = title ?: "",
    originalTitle = originalTitle ?: "",
    releaseDate = releaseDate ?: "",
    video = video ?: false,
    name = name ?: "",
    originalName = originalName ?: "",
    firstAirDate = firstAirDate ?: "",
    originCountry = originCountry ?: emptyList(),
)

fun GenreDTO.toDomain(): Genre = Genre(
    id = this.id,
    name = this.name,
)
