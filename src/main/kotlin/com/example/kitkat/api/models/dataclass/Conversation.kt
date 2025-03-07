package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: Int,
    val participantIds: List<Int>,
    val createdAt: String
)