package com.example.datastore

import com.example.datastore.di.TestDataStoreModule.testUserPreferencesDataStore
import com.example.model.DarkThemeConfig
import com.example.model.MediaItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class IMDBPreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var preference: IMDBPreferencesDataSource

    @TempDir
    lateinit var tmpDir: File

    @BeforeEach
    fun setUp() {
        preference = IMDBPreferencesDataSource(
            testUserPreferencesDataStore(
                file = File(tmpDir, "user_preferences_test.pb"),
                coroutineScope = testScope
            )
        )
    }

    @Test
    fun `GIVEN default state WHEN initial flow read THEN dark theme defaults to FOLLOW_SYSTEM`() =
        testScope.runTest {
            val userData = preference.userData.first()

            assertEquals(DarkThemeConfig.FOLLOW_SYSTEM, userData.darkThemeConfig)
        }

    @Test
    fun `GIVEN dark theme config set to LIGHT WHEN setDarkThemeConfig called THEN userData reflects LIGHT`() =
        testScope.runTest {
            preference.setDarkThemeConfig(DarkThemeConfig.LIGHT)

            val userData = preference.userData.first()

            assertEquals(DarkThemeConfig.LIGHT, userData.darkThemeConfig)
        }

    @Test
    fun `GIVEN empty watchlist WHEN setWatchList called with items THEN userData watchList contains all items`() =
        testScope.runTest {
            val movie1 = createSampleMediaItem(id = 1, title = "Inception")
            val movie2 = createSampleMediaItem(id = 2, title = "Interstellar")
            val movies = listOf(movie1, movie2)

            preference.setWatchList(movies)

            val userData = preference.userData.first()
            assertEquals(2, userData.watchListMovies.size)
            assertEquals(movies, userData.watchListMovies)
        }

    @Test
    fun `GIVEN existing watchlist items WHEN setWatchList called with empty list THEN watchlist is cleared`() =
        testScope.runTest {
            val movie = createSampleMediaItem(id = 1, title = "Inception")
            preference.setWatchList(listOf(movie))

            preference.setWatchList(emptyList())

            val userData = preference.userData.first()
            assertTrue(userData.watchListMovies.isEmpty())
        }

    private fun createSampleMediaItem(
        id: Int = 101,
        title: String = "Inception"
    ): MediaItem = MediaItem(
        id = id,
        mediaType = "movie",
        adult = false,
        backdropPath = "/backdrop.jpg",
        posterPath = "/poster.jpg",
        overview = "A thief who steals corporate secrets...",
        originalLanguage = "en",
        genreIds = listOf(28, 878),
        popularity = 8.5,
        voteAverage = 8.8,
        voteCount = 20000,
        title = title,
        originalTitle = title,
        releaseDate = "2010-07-16",
        video = false,
        name = null,
        originalName = null,
        firstAirDate = null,
        originCountry = listOf("US")
    )
}
