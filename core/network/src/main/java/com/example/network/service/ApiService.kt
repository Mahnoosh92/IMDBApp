package com.example.network.service

import com.example.network.model.GenreResponseDTO
import com.example.network.model.NowPlayingMediaResponseDto
import com.example.network.model.PopularMovieResponseDto
import com.example.network.model.TrendingMovieResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("3/trending/all/day")
    suspend fun getTrendingMovies(@Query("language") language: String = "en-US"): Response<TrendingMovieResponseDto>

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): Response<NowPlayingMediaResponseDto>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): Response<PopularMovieResponseDto>

    @GET("3/genre/movie/list")
    suspend fun getGenres(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): Response<GenreResponseDTO>
}
