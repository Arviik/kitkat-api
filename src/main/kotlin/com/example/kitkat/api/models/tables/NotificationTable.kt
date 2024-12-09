package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object NotificationTable : IntIdTable() {
    val user = reference("user", UserTable)
    val type = varchar("type", 255)
    val relatedUser = reference("related_user", UserTable)
    val video = reference("video", VideoTable)
    val isRead = bool("is_read")
    val createdAt = timestamp("created_at")
}
