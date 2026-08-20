package com.example.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class DatesDto(
    @SerialName("maximum")
    val maximum: String,
    @SerialName("minimum")
    val minimum: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NowPlayingMediaResponseDto(
    @SerialName("dates")
    val dates: DatesDto,
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MovieItemDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)
