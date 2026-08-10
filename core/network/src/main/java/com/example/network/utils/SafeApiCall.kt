package com.example.network.utils

import com.example.network.model.NetworkException
import retrofit2.Response
import java.io.IOException

suspend fun <T, R> safeApiCall(apiCall: suspend () -> Response<T>, transform: (T) -> R): Result<R> = try {
    val response = apiCall()
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            Result.success(transform(body))
        } else {
            Result.failure(NetworkException.Unknown(NullPointerException("Response body was null")))
        }
    } else {
        val errorMsg = response.errorBody()?.string().orEmpty()
        val exception =
            when (response.code()) {
                400 -> NetworkException.BadRequest(errorMsg)
                401, 403 -> NetworkException.Unauthorized(errorMsg)
                in 500..599 -> NetworkException.ServerError(response.code(), errorMsg)
                else -> NetworkException.ApiError(response.code(), errorMsg)
            }
        Result.failure(exception)
    }
} catch (e: IOException) {
    Result.failure(NetworkException.NoInternet(e))
} catch (e: Exception) {
    Result.failure(NetworkException.Unknown(e))
}
