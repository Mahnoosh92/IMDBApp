package com.example.network.model

sealed class NetworkException(override val message: String, val statusCode: Int? = null) : Exception(message) {
    class Unauthorized(message: String = "Unauthorized access. Check API key.") : NetworkException(message, 401)

    class BadRequest(message: String = "Bad request.") : NetworkException(message, 400)

    class ServerError(statusCode: Int, message: String = "Server error occurred.") :
        NetworkException(message, statusCode)

    class ApiError(statusCode: Int, message: String) : NetworkException(message, statusCode)

    class NoInternet(override val cause: Throwable? = null) :
        NetworkException("No internet connection or timeout occurred.", null)

    class Unknown(override val cause: Throwable? = null) :
        NetworkException("An unexpected error occurred.", null)
}
