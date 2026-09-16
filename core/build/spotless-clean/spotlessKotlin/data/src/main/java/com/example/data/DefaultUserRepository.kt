package com.example.data

import com.example.common.dispatcher.Dispatcher
import com.example.common.dispatcher.Dispatchers
import com.example.datastore.IMDBPreferencesDataSource
import com.example.model.DarkThemeConfig
import com.example.model.MovieItem
import com.example.model.UserData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val localDataSource: IMDBPreferencesDataSource,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : UserRepository {
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) = withContext(dispatcher) {
        localDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun getDarkThemeConfig(): DarkThemeConfig = withContext(dispatcher) {
        localDataSource.getDarkThemeConfig()
    }

    override suspend fun setWatchList(watchList: List<MovieItem>) = withContext(dispatcher) {
        localDataSource.setWatchList(watchList)
    }

    override suspend fun addWatchItem(watchItem: MovieItem) = withContext(dispatcher) {
        localDataSource.addWatchItem(watchItem)
    }

    override suspend fun removeWatchItem(watchItem: MovieItem) = withContext(dispatcher) {
        localDataSource.removeWatchItem(watchItem)
    }

    override val userData: Flow<UserData> = localDataSource.userData
}
