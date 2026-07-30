package com.example.network.datasource

import com.example.network.model.MediaItemDto

interface MovieRemoteDatasource {
    suspend fun getTrendingMovies(): Result<List<MediaItemDto>>
    suspend fun getNowPlayingMovies(page: Int): Result<List<MediaItemDto>>
    suspend fun getPopularMovies(page: Int): Result<List<MediaItemDto>>
}