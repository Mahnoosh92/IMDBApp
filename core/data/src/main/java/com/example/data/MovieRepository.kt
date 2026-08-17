package com.example.data

import com.example.model.MovieItem

interface MovieRepository {
    suspend fun getTrendingMovies(): Result<List<MovieItem>>

    suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItem>>

    suspend fun getPopularMovies(page: Int): Result<List<MovieItem>>
}
