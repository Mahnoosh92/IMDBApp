package com.example.data

import com.example.data.mapper.mapListToDomain
import com.example.data.mapper.toDomain
import com.example.model.Genre
import com.example.model.MovieItem
import com.example.network.datasource.MovieRemoteDatasource
import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(private val remoteDataSource: MovieRemoteDatasource) : MovieRepository {
    override suspend fun getTrendingMovies(): Result<List<MovieItem>> = remoteDataSource.getTrendingMovies().mapListToDomain(MovieItemDto::toDomain)

    override suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItem>> = remoteDataSource.getNowPlayingMovies(page = page).mapListToDomain(MovieItemDto::toDomain)

    override suspend fun getPopularMovies(page: Int): Result<List<MovieItem>> = remoteDataSource.getPopularMovies(page = page).mapListToDomain(MovieItemDto::toDomain)
    override suspend fun getGenres(page: Int): Result<List<Genre>> = remoteDataSource.getGenres().mapListToDomain(GenreDTO::toDomain)
}
