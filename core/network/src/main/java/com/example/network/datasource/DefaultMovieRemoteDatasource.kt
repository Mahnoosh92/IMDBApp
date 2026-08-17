package com.example.network.datasource

import com.example.common.dispatcher.Dispatcher
import com.example.common.dispatcher.Dispatchers
import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto
import com.example.network.service.ApiService
import com.example.network.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMovieRemoteDatasource
@Inject
constructor(
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val apiService: ApiService,
) : MovieRemoteDatasource {
    override suspend fun getTrendingMovies(): Result<List<MovieItemDto>> = withContext(dispatcher) {
        safeApiCall({ apiService.getTrendingMovies() }) { it.results }
    }

    override suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItemDto>> = withContext(dispatcher) {
        safeApiCall({ apiService.getNowPlayingMovies() }) { it.results }
    }

    override suspend fun getPopularMovies(page: Int): Result<List<MovieItemDto>> = withContext(dispatcher) {
        safeApiCall({ apiService.getPopularMovies() }) { it.results }
    }

    override suspend fun getGenres(): Result<List<GenreDTO>> = withContext(dispatcher) {
        safeApiCall({ apiService.getGenres() }) { it.genres }
    }
}
