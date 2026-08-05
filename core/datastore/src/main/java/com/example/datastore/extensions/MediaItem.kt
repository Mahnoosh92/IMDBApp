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

fun MediaItem.asInternalModel(): com.example.core.datastore.MediaItem {
    return com.example.core.datastore.MediaItem.newBuilder()
        .setId(id)
        .setMediaType(mediaType)
        .setAdult(adult)
        .apply {
            backdropPath?.let { setBackdropPath(it) }
            posterPath?.let { setPosterPath(it) }
            title?.let { setTitle(it) }
            originalTitle?.let { setOriginalTitle(it) }
            releaseDate?.let { setReleaseDate(it) }
            video?.let { setVideo(it) }
            name?.let { setName(it) }
            originalName?.let { setOriginalName(it) }
            firstAirDate?.let { setFirstAirDate(it) }
        }
        .setOverview(overview)
        .setOriginalLanguage(originalLanguage)
        .addAllGenreIds(genreIds)
        .setPopularity(popularity)
        .setVoteAverage(voteAverage)
        .setVoteCount(voteCount)
        .addAllOriginCountry(originCountry ?: emptyList())
        .build()
}