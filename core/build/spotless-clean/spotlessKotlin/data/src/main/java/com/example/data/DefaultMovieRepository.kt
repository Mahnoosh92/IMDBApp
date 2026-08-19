package com.example.data

import com.example.common.dispatcher.Dispatcher
import com.example.common.dispatcher.Dispatchers
import com.example.data.mapper.mapListToDomain
import com.example.data.mapper.toDomain
import com.example.model.Genre
import com.example.model.MovieItem
import com.example.network.datasource.MovieRemoteDatasource
import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDatasource,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : MovieRepository {
    override suspend fun getTrendingMovies(): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.getTrendingMovies().mapListToDomain(MovieItemDto::toDomain) }

    override suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.getNowPlayingMovies(page = page).mapListToDomain(MovieItemDto::toDomain) }

    override suspend fun getPopularMovies(page: Int): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.getPopularMovies(page = page).mapListToDomain(MovieItemDto::toDomain) }
    override suspend fun getGenres(page: Int): Result<List<Genre>> = withContext(dispatcher) { remoteDataSource.getGenres().mapListToDomain(GenreDTO::toDomain) }
}
