package com.example.data.extension

import com.example.model.IMDBException
import com.example.network.model.NetworkException

fun NetworkException.toIMDBException(): IMDBException {
    return when (this) {
        is NetworkException.Unauthorized -> IMDBException.Unauthorized(this.message)
        is NetworkException.BadRequest -> IMDBException.InvalidRequest(this.message)
        is NetworkException.ServerError -> IMDBException.ServerFailure(this.statusCode)
        is NetworkException.ApiError -> IMDBException.ServerFailure(this.statusCode)
        is NetworkException.NoInternet -> IMDBException.NoConnection("No internet connection or timeout occurred.")
        is NetworkException.Unknown -> IMDBException.Unknown(this.cause)
    }
}
