package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val author: String,
    val video: String,
    val text: String,
    val createdAt: String,
    val likesCount: Int
)
