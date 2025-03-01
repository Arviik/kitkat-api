package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class LikeDTO(
    val videoId: Int,
    val userId: Int
)
