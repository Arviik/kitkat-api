package com.example.kitkat.api.models.repository

import com.example.kitkat.api.models.dao.CommentDAO
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dao.VideoDAO
import com.example.kitkat.api.models.dataclass.CommentDTO
import com.example.kitkat.api.models.dataclass.UserDTO
import com.example.kitkat.api.models.tables.Comments
import com.example.kitkat.api.services.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class CommentRepository : Repository<CommentDTO> {
    override suspend fun all(): List<CommentDTO> = suspendTransaction {
        CommentDAO.all().map(::daoToModel)
    }

    override suspend fun byId(id: Int): CommentDTO? = suspendTransaction {
        CommentDAO
            .find { (Comments.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun add(model: CommentDTO) {
        val user = UserDAO.findById(model.authorId) ?: throw IllegalArgumentException("Not found")
        val video = VideoDAO.findById(model.videoId) ?: throw IllegalArgumentException("Not found")

        CommentDAO.new {
            author = user
            this.video = video
            text = model.text
            createdAt = Instant.parse(model.createdAt)
            likesCount = model.likesCount
        }
    }

    override suspend fun update(id: Int, model: CommentDTO): Boolean = suspendTransaction {
        val user = UserDAO.findById(model.authorId) ?: throw IllegalArgumentException("Not found")
        val video = VideoDAO.findById(model.videoId) ?: throw IllegalArgumentException("Not found")

        val rowsUpdated = CommentDAO.findByIdAndUpdate(id) {
            it.author = user
            it.video = video
            it.text = model.text
            it.createdAt = Instant.parse(model.createdAt)
            it.likesCount = model.likesCount
        }
        rowsUpdated != null
    }

    override suspend fun remove(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = Comments.deleteWhere {
            Comments.id eq id
        }
        rowsDeleted == 1
    }

    fun daoToModel(dao: CommentDAO) = CommentDTO(
        dao.id.value,
        dao.author.id.value,
        dao.video.id.value,
        dao.text,
        dao.createdAt.toString(),
        dao.likesCount
    )
}