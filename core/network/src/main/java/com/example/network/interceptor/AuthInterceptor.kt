package com.example.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest =
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $apiKey")
                .header("accept", "application/json")
                .build()

        return chain.proceed(newRequest)
    }
}
