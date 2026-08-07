package com.example.datastore.extensions

import com.example.model.MediaItem

fun com.example.core.datastore.MediaItem.asExternalModel(): MediaItem {
    return MediaItem(
        id = id,
        mediaType = mediaType,
        adult = adult,
        backdropPath = backdropPath.ifEmpty { null },
        posterPath = posterPath.ifEmpty { null },
        overview = overview,
        originalLanguage = originalLanguage,
        genreIds = genreIdsList,
        popularity = popularity,
        voteAverage = voteAverage,
        voteCount = voteCount,
        title = title.ifEmpty { null },
        originalTitle = originalTitle.ifEmpty { null },
        releaseDate = releaseDate.ifEmpty { null },
        video = video,
        name = name.ifEmpty { null },
        originalName = originalName.ifEmpty { null },
        firstAirDate = firstAirDate.ifEmpty { null },
        originCountry = originCountryList
    )
}

fun MediaItem.asInternalModel(): com.example.core.datastore.MediaItem =
    com.example.core.datastore.MediaItem.newBuilder().apply {
        id = this@asInternalModel.id
        mediaType = this@asInternalModel.mediaType
        adult = this@asInternalModel.adult

        // Use default empty string fallback so proto sets the value cleanly
        backdropPath = this@asInternalModel.backdropPath ?: ""
        posterPath = this@asInternalModel.posterPath ?: ""
        title = this@asInternalModel.title ?: ""
        originalTitle = this@asInternalModel.originalTitle ?: ""
        releaseDate = this@asInternalModel.releaseDate ?: ""
        video = this@asInternalModel.video ?: false
        name = this@asInternalModel.name ?: ""
        originalName = this@asInternalModel.originalName ?: ""
        firstAirDate = this@asInternalModel.firstAirDate ?: ""

        overview = this@asInternalModel.overview
        originalLanguage = this@asInternalModel.originalLanguage
        addAllGenreIds(genreIds)
        popularity = this@asInternalModel.popularity
        voteAverage = this@asInternalModel.voteAverage
        voteCount = this@asInternalModel.voteCount
        addAllOriginCountry(originCountry ?: emptyList())
    }.build()