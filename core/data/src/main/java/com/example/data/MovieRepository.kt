package com.example.data

import com.example.model.Genre
import com.example.model.MovieItem

interface MovieRepository {
    suspend fun getTrendingMovies(): Result<List<MovieItem>>

    suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItem>>

    suspend fun getPopularMovies(page: Int): Result<List<MovieItem>>
    suspend fun getGenres(page: Int): Result<List<Genre>>
    suspend fun discoverMovies(genre: String): Result<List<MovieItem>>
}
