package com.example.kitkat.api.services

import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dao.VideoDAO
import com.example.kitkat.api.models.dataclass.VideoDTO
import com.example.kitkat.api.models.tables.Videos
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class VideoService(private val database: Database) {

    init {
        transaction(database) {
            SchemaUtils.create(Videos)
        }
    }

    suspend fun create(videoDTO: VideoDTO): Int = dbQuery {
        VideoDAO.new {
            title = videoDTO.title
            duration = videoDTO.duration
            author = UserDAO.findById(videoDTO.authorId) ?: throw IllegalArgumentException("Author not found")
            videoUrl = videoDTO.videoUrl
            viewCount = videoDTO.viewCount
            likeCount = videoDTO.likeCount
            commentCount = videoDTO.commentCount
            createdAt = Instant.parse(videoDTO.createdAt) // Conversion String -> Instant
            isPublic = videoDTO.isPublic
        }.id.value
    }

    suspend fun read(id: Int): VideoDTO? = dbQuery {
        VideoDAO.findById(id)?.let {
            VideoDTO(
                id = it.id.value,
                title = it.title,
                duration = it.duration,
                authorId = it.author.id.value,
                videoUrl = it.videoUrl,
                viewCount = it.viewCount,
                likeCount = it.likeCount,
                commentCount = it.commentCount,
                createdAt = it.createdAt.toString(),
                isPublic = it.isPublic,
            )
        }
    }

    suspend fun update(id: Int, videoDTO: VideoDTO) = dbQuery {
        VideoDAO.findById(id)?.apply {
            title = videoDTO.title
            duration = videoDTO.duration
            videoUrl = videoDTO.videoUrl
            viewCount = videoDTO.viewCount
            likeCount = videoDTO.likeCount
            commentCount = videoDTO.commentCount
            isPublic = videoDTO.isPublic
        }
    }

    suspend fun delete(id: Int) = dbQuery {
        VideoDAO.findById(id)?.delete()
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}