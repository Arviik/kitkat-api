package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class SearchQueryDTO(
    val userId: Int,
    val query: String,
    val createdAt: String
)
