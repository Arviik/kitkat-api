package com.example.kitkat.api.models.repository

import com.example.kitkat.api.models.dao.CommentDAO
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dao.VideoDAO
import com.example.kitkat.api.models.dataclass.Comment
import com.example.kitkat.api.models.dataclass.User
import com.example.kitkat.api.models.tables.Comments
import com.example.kitkat.api.services.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class CommentRepository() : Repository<Comment> {
    override suspend fun all(): List<Comment> = suspendTransaction {
        CommentDAO.all().map(::daoToModel)
    }

    override suspend fun byId(id: Int): Comment? = suspendTransaction {
        CommentDAO
            .find { (Comments.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun add(model: Comment) {
        val user = UserDAO.findById(model.author) ?: throw IllegalArgumentException("Not found")
        val video = VideoDAO.findById(model.author) ?: throw IllegalArgumentException("Not found")

        CommentDAO.new {
            author = user
            this.video = video
            text = model.text
            createdAt = Instant.parse(model.createdAt)
            likesCount = model.likesCount
        }
    }

    override suspend fun update(id: Int, model: Comment): Boolean = suspendTransaction {
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
        val rowsDeleted = CommentTable.deleteWhere {
            CommentTable.id eq id
        }
        rowsDeleted == 1
    }

    fun daoToModel(dao: CommentDAO) = Comment(
        dao.author.id.value,
        dao.video.id.value,
        dao.text,
        dao.createdAt.toString(),
        dao.likesCount
    )
}