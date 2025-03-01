package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDTO(
    var userId: Int,
    var type: String,
    var relatedUserId: Int,
    var videoId: Int,
    var isRead: Boolean,
    var createdAt: String
)
