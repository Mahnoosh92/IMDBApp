package com.example.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularMediaResponseDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MediaItemDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)
