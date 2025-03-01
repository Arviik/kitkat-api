package com.example.kitkat.api.services

import com.example.kitkat.api.models.dao.CommentDAO
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dao.VideoDAO
import com.example.kitkat.api.models.dataclass.CommentDTO
import com.example.kitkat.api.models.tables.Comments
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class CommentService(private val database: Database) {

    // 🔹 Créer un commentaire
    fun create(commentDTO: CommentDTO): Int = transaction(database) {
        CommentDAO.new {
            author = UserDAO.findById(commentDTO.authorId)
                ?: throw IllegalArgumentException("Author not found")

            video = VideoDAO.findById(commentDTO.videoId)
                ?: throw IllegalArgumentException("Video not found")

            text = commentDTO.text
            createdAt = Instant.parse(commentDTO.createdAt) // ✅ Convertir String en Instant
            likesCount = commentDTO.likesCount
        }.id.value
    }

    // 🔹 Récupérer un commentaire par son ID
    fun read(id: Int): CommentDTO? = transaction(database) {
        CommentDAO.findById(id)?.let { comment ->
            CommentDTO(
                id = comment.id.value,
                authorId = comment.author.id.value,
                videoId = comment.video.id.value,
                text = comment.text,
                createdAt = comment.createdAt.toString(),
                likesCount = comment.likesCount,
                authorName = comment.author.name // ✅ Ajout du nom de l'auteur
            )
        }
    }


    // 🔹 Récupérer tous les commentaires liés à une vidéo avec le nom de l’auteur
    fun getCommentsWithAuthor(videoId: Int): List<CommentDTO> = transaction(database) {
        CommentDAO.find { Comments.video eq videoId }
            .map { comment ->
                val authorName = comment.author.name // ✅ Récupération du nom de l’auteur
                comment.toCommentDTOWithAuthor(authorName)
            }
    }

    // 🔹 Mettre à jour un commentaire
    fun update(id: Int, commentDTO: CommentDTO) = transaction(database) {
        val comment = CommentDAO.findById(id) ?: throw IllegalArgumentException("Comment not found")
        comment.apply {
            text = commentDTO.text
            createdAt = Instant.parse(commentDTO.createdAt)
            likesCount = commentDTO.likesCount
        }
    }

    // 🔹 Supprimer un commentaire
    fun delete(id: Int) = transaction(database) {
        CommentDAO.findById(id)?.delete()
    }

    // 🔹 Convertir un DAO en DTO (avec auteur)
    private fun CommentDAO.toCommentDTOWithAuthor(authorName: String): CommentDTO {
        return CommentDTO(
            id = this.id.value,
            authorId = this.author.id.value,
            videoId = this.video.id.value,
            text = this.text,
            createdAt = this.createdAt.toString(),
            likesCount = this.likesCount,
            authorName = authorName
        )
    }
}
