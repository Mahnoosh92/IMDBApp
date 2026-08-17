package com.example.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor(private val context: Context, private val isMockEnabled: Boolean) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isMockEnabled) {
            return chain.proceed(chain.request())
        }

        val uri = chain.request().url.toUri()
        val path = uri.path

        val jsonFileName =
            when {
                path.endsWith("/trending/all/day") -> "trending_movies.json"
                path.endsWith("/movie/now_playing") -> "now_playing_movies.json"
                path.endsWith("/movie/popular") -> "popular_movies.json"
                path.endsWith("/genre/movie/listr") -> "genres.json"
                else -> null
            }

        if (jsonFileName != null) {
            try {
                val jsonString = context.assets.open(jsonFileName).bufferedReader().use { it.readText() }

                return Response.Builder()
                    .code(200)
                    .message("OK (Mocked)")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(jsonString.toResponseBody("application/json".toMediaType()))
                    .addHeader("content-type", "application/json")
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return chain.proceed(chain.request())
    }
}
