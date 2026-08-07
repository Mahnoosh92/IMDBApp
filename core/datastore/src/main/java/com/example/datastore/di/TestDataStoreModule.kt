package com.example.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.example.common.di.ApplicationScope
import com.example.core.datastore.UserPreferences
import com.example.datastore.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import java.io.File
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
internal object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<UserPreferences> =
        tmpFolder.testUserPreferencesDataStore(
            coroutineScope = scope,
            userPreferencesSerializer = userPreferencesSerializer,
        )

    fun TemporaryFolder.testUserPreferencesDataStore(
        coroutineScope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer = UserPreferencesSerializer(),
    ) = testUserPreferencesDataStore(
        file = newFile("user_preferences_test.pb"),
        coroutineScope = coroutineScope,
        userPreferencesSerializer = userPreferencesSerializer,
    )

    fun testUserPreferencesDataStore(
        file: File,
        coroutineScope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer = UserPreferencesSerializer(),
    ) = DataStoreFactory.create(
        serializer = userPreferencesSerializer,
        scope = coroutineScope,
    ) {
        file
    }
}
