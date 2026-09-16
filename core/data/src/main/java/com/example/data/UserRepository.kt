package com.example.data

import com.example.model.DarkThemeConfig
import com.example.model.MovieItem
import com.example.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun getDarkThemeConfig(): DarkThemeConfig

    suspend fun removeWatchItem(watchItem: MovieItem)
    suspend fun setWatchList(watchList: List<MovieItem>)
    suspend fun addWatchItem(watchItem: MovieItem)

    val userData: Flow<UserData>
}
