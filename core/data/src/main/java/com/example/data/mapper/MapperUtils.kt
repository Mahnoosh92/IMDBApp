package com.example.data.mapper

import com.example.data.extension.toIMDBException
import com.example.model.IMDBException
import com.example.network.model.NetworkException

inline fun <T, R> Result<T>.mapToDomain(crossinline transform: (T) -> R): Result<R> {
    return fold(
        onSuccess = { data ->
            try {
                Result.success(transform(data))
            } catch (e: Exception) {
                Result.failure(IMDBException.Unknown(e))
            }
        },
        onFailure = { throwable ->
            val domainException = when (throwable) {
                is NetworkException -> throwable.toIMDBException()
                is IMDBException -> throwable
                else -> IMDBException.Unknown(throwable)
            }
            Result.failure(domainException)
        },
    )
}

inline fun <T, R> Result<List<T>>.mapListToDomain(crossinline transform: (T) -> R): Result<List<R>> {
    return mapToDomain { list -> list.map(transform) }
}
