package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val authorId: Int,
    val videoId: Int,
    val text: String,
    val createdAt: String,
    val likesCount: Int
)
