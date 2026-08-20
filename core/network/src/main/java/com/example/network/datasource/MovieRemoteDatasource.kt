package com.example.network.datasource

import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto

interface MovieRemoteDatasource {
    suspend fun getTrendingMovies(): Result<List<MovieItemDto>>

    suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItemDto>>

    suspend fun getPopularMovies(page: Int): Result<List<MovieItemDto>>
    suspend fun getGenres(): Result<List<GenreDTO>>
    suspend fun discoverMovies(genre: String): Result<List<MovieItemDto>>
}
