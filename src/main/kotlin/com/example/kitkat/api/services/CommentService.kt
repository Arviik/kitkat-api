package com.example.kitkat.api.services

import com.example.kitkat.api.models.dao.CommentDAO
import com.example.kitkat.api.models.dataclass.Comment
import com.example.kitkat.api.models.tables.CommentTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class CommentService(): Service<CommentDAO> {
    init {
        transaction {
            SchemaUtils.create(CommentTable)
        }
    }

    override fun create(comment: CommentDAO): Int {
        return CommentDAO.new {
            author = comment.author
            video = comment.video
            text = comment.text
            createdAt = comment.createdAt
            likesCount = comment.likesCount
        }.id.value
    }

    override fun read(id: Int): CommentDAO? {
        return CommentDAO.findById(id)
    }

    override fun update(id: Int, comment: CommentDAO) {
        CommentDAO.findByIdAndUpdate(id) {
            it.author = comment.author
            it.video = comment.video
            it.text = comment.text
            it.createdAt = comment.createdAt
            it.likesCount = comment.likesCount
        }
    }

    override fun delete(id: Int) {
        CommentDAO.findById(id)?.delete()
    }

    fun daoToModel(dao: CommentDAO) = Comment(
        dao.author.name,
        dao.video.videoUrl,
        dao.text,
        dao.createdAt.toString(),
        dao.likesCount
    )
}
