// MessageTable.kt
package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MessageTable : IntIdTable() {
    val sender = reference("sender", UserTable)
    val receiver = reference("receiver", UserTable)
    val content = text("content")
    val createdAt = timestamp("created_at")
    val conversation = reference("conversation", ConversationTable)
    val isSystemMessage = bool("is_system_message").default(false)
}