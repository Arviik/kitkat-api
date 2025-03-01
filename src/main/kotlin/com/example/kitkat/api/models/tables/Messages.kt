// MessageTable.kt
package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MessageTable : IntIdTable() {
    val sender = reference("sender", Users)
    val receiver = reference("receiver", Users)
    val content = text("content")
    val createdAt = timestamp("created_at")
    val conversation = reference("conversation", Conversations)
    val isSystemMessage = bool("is_system_message").default(false)
}