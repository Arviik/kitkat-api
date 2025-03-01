package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class CommentDTO(
    val id: Int? = null,
    val authorId: Int,
    val videoId: Int,
    val text: String,
    val createdAt: String, // ✅ String pour compatibilité avec JSON
    val likesCount: Int = 0,
    val authorName: String? = null // ✅ Pour afficher le nom de l'auteur
)
