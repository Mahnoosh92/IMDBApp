package com.example.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.core.datastore.DarkThemeConfigProto
import com.example.core.datastore.UserPreferences
import com.example.core.datastore.WatchList
import com.example.core.datastore.copy
import com.example.datastore.extensions.asExternalModel
import com.example.datastore.extensions.asInternalModel
import com.example.model.DarkThemeConfig
import com.example.model.MediaItem
import com.example.model.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class IMDBPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                        ->
                        DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT

                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
                watchListMovies = it.watchList.itemsList.map(com.example.core.datastore.MediaItem::asExternalModel),
            )
        }

    suspend fun setWatchList(watchList: List<MediaItem>) {
        try {
            userPreferences.updateData { currentPreferences ->
                currentPreferences.copy {
                    this.watchList = WatchList.newBuilder()
                        .addAllItems(watchList.map(MediaItem::asInternalModel))
                        .build()
                }
            }
        } catch (ioException: IOException) {
            Log.e("IMDBPreferencesDS", "Failed to update watchlist preferences", ioException)
        }
    }

    suspend fun addWatchItem(watchItem: MediaItem) {
        try {
            userPreferences.updateData { currentPreferences ->
                val currentItems = currentPreferences.watchList.itemsList.toMutableList()

                if (currentItems.none { it.id == watchItem.id }) {
                    currentItems.add(watchItem.asInternalModel())
                }

                currentPreferences.copy {
                    this.watchList = WatchList.newBuilder()
                        .addAllItems(currentItems)
                        .build()
                }
            }
        } catch (ioException: IOException) {
            Log.e("IMDBPreferencesDS", "Failed to add item to watchlist", ioException)
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM ->
                        DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM

                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }
}