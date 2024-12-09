package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Sound(
    val title: String,
    val authorId: Int,
    val duration: Int,
    val url: String,
    val usageCount: Int
)
