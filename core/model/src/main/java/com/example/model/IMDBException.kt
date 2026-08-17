package com.example.model

sealed class IMDBException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause) {

    class Unauthorized(message: String = "Unauthorized access.") : IMDBException(message)

    class InvalidRequest(message: String = "Invalid input provided.") : IMDBException(message)

    data class ServerFailure(val code: Int?) : IMDBException("Server error ($code)")

    class NoConnection(message: String = "No internet connection.") : IMDBException(message)

    data class Unknown(override val cause: Throwable? = null) : IMDBException("Unexpected error", cause)
}
