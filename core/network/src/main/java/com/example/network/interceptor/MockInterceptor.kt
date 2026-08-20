package com.example.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor(
    private val context: Context,
    private val isMockEnabled: Boolean,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!isMockEnabled) {
            return chain.proceed(request)
        }

        // Match on encodedPath or full URL string to avoid string-matching misses
        val path = request.url.encodedPath

        val jsonFileName = when {
            path.contains("trending/all/day") -> "trending_movies.json"
            path.contains("movie/now_playing") -> "now_playing_movies.json"
            path.contains("movie/popular") -> "popular_movies.json"
            path.contains("genre/movie/list") -> "genres.json"
            path.contains("discover/movie") -> "now_playing_movies.json"
            else -> null
        }

        if (jsonFileName == null) {
            return chain.proceed(request)
        }

        return try {
            val jsonString = context.assets.open(jsonFileName).bufferedReader().use { it.readText() }
            Response.Builder()
                .code(200)
                .message("OK (Mocked)")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(jsonString.toResponseBody("application/json; charset=utf-8".toMediaType()))
                .header("content-type", "application/json")
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Builder()
                .code(500)
                .message("Mock asset $jsonFileName not found: ${e.localizedMessage}")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body("""{"status_message": "${e.localizedMessage}"}""".toResponseBody("application/json".toMediaType()))
                .build()
        }
    }
}
