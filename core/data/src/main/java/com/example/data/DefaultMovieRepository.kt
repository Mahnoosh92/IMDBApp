package com.example.data

import com.example.common.dispatcher.Dispatcher
import com.example.common.dispatcher.Dispatchers
import com.example.data.mapper.mapListToDomain
import com.example.data.mapper.toDomain
import com.example.datastore.IMDBPreferencesDataSource
import com.example.model.Genre
import com.example.model.MovieItem
import com.example.model.UserData
import com.example.network.datasource.MovieRemoteDatasource
import com.example.network.model.GenreDTO
import com.example.network.model.MovieItemDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDatasource,
    private val localDataSource: IMDBPreferencesDataSource,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : MovieRepository {

    // In-memory cache for genres
    private var cachedGenres: List<Genre>? = null
    private val genresMutex = Mutex()

    override suspend fun getTrendingMovies(): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.getTrendingMovies().mapListToDomain(MovieItemDto::toDomain) }

    override suspend fun getNowPlayingMovies(page: Int): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.getNowPlayingMovies(page = page).mapListToDomain(MovieItemDto::toDomain) }

    override suspend fun getPopularMovies(page: Int): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.getPopularMovies(page = page).mapListToDomain(MovieItemDto::toDomain) }
    override suspend fun getGenres(page: Int): Result<List<Genre>> = withContext(dispatcher) {
        cachedGenres?.let { return@withContext Result.success(it) }

        genresMutex.withLock {
            cachedGenres?.let { return@withContext Result.success(it) }
            val result = remoteDataSource.getGenres().mapListToDomain(GenreDTO::toDomain)
            result.onSuccess { genres ->
                cachedGenres = genres
            }
            result
        }
        remoteDataSource.getGenres().mapListToDomain(GenreDTO::toDomain)
    }
    override suspend fun discoverMovies(genre: String): Result<List<MovieItem>> = withContext(dispatcher) { remoteDataSource.discoverMovies(genre = genre).mapListToDomain(MovieItemDto::toDomain) }
    override suspend fun removeWatchItem(watchItem: MovieItem) = withContext(dispatcher) {
        localDataSource.removeWatchItem(watchItem)
    }

    override suspend fun setWatchList(watchList: List<MovieItem>) = withContext(dispatcher) {
        localDataSource.setWatchList(watchList)
    }

    override suspend fun addWatchItem(watchItem: MovieItem) = withContext(dispatcher) {
        localDataSource.addWatchItem(watchItem)
    }
    override val userData: Flow<UserData> = localDataSource.userData
}
