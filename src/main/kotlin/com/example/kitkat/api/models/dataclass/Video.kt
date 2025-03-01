package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    var title: String,
    var duration: Int,
    var authorId: Int,
    var videoUrl: String,
    var viewCount: Int,
    var likeCount: Int,
    var commentCount: Int,
    var createdAt: String,
    var isPublic: Boolean,
    var soundId: Int
)
