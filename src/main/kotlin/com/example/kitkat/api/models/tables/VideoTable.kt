package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object VideoTable : IntIdTable() {
    val title = varchar("title", 255)
    val duration = integer("duration")
    val author = reference("user", UserTable)
    val videoUrl = text("video_url")
    val viewCount = integer("view_count")
    val likeCount = integer("like_count")
    val commentCount = integer("comment_count")
    val createdAt = timestamp("created_at")
    val isPublic = bool("is_public")
    val sound = reference("sound", SoundTable)
}
