package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val createdAt: String,
    val conversationId: Int,
    val isSystemMessage: Boolean = false
)