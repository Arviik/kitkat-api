package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object CommentTable : IntIdTable() {
    val author = reference("user", UserTable)
    val video = reference("video", VideoTable)
    val text = text("text")
    val createdAt = timestamp("created_at")
    val likesCount = integer("likes_count")
}
