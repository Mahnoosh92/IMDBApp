package com.example.network.datasource

import com.example.network.model.MediaItemDto
import com.example.network.model.NetworkException
import com.example.network.model.TrendingMediaResponseDto
import com.example.network.service.ApiService
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.Response
import java.util.stream.Stream

class DefaultMovieRemoteDatasourceTest {
    private val apiService: ApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataSource: MovieRemoteDatasource

    @BeforeEach
    fun setUp() {
        dataSource =
            DefaultMovieRemoteDatasource(
                dispatcher = testDispatcher,
                apiService = apiService,
            )
    }

    @Test
    fun `GIVEN 200 OK response WHEN getTrendingMovies is called THEN returns Result success with items`() = runTest(testDispatcher) {
        val mockItems = listOf(mockk<MediaItemDto>(), mockk<MediaItemDto>())
        val responseDto =
            TrendingMediaResponseDto(
                results = mockItems,
                page = 1,
                totalPages = 1,
                totalResults = 1,
            )
        coEvery { apiService.getTrendingMovies() } returns Response.success(responseDto)

        val result = dataSource.getTrendingMovies()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(mockItems)
        coVerify(exactly = 1) { apiService.getTrendingMovies() }
    }

    @ParameterizedTest(name = "HTTP {0} maps to expected exception type {1}")
    @MethodSource("provideHttpErrorScenarios")
    fun `GIVEN HTTP status code error WHEN getTrendingMovies is called THEN returns corresponding NetworkException`(statusCode: Int, expectedExceptionClass: Class<out NetworkException>) = runTest(testDispatcher) {
        val errorResponseBody = "{\"message\":\"An error occurred\"}".toResponseBody()
        val errorResponse = Response.error<TrendingMediaResponseDto>(statusCode, errorResponseBody)
        coEvery { apiService.getTrendingMovies() } returns errorResponse

        val result = dataSource.getTrendingMovies()

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(expectedExceptionClass)
    }

    companion object {
        @JvmStatic
        fun provideHttpErrorScenarios(): Stream<Arguments> = Stream.of(
            Arguments.of(400, NetworkException.BadRequest::class.java),
            Arguments.of(401, NetworkException.Unauthorized::class.java),
            Arguments.of(403, NetworkException.Unauthorized::class.java),
            Arguments.of(500, NetworkException.ServerError::class.java),
            Arguments.of(503, NetworkException.ServerError::class.java),
            Arguments.of(404, NetworkException.ApiError::class.java),
            Arguments.of(429, NetworkException.ApiError::class.java),
        )
    }
}
