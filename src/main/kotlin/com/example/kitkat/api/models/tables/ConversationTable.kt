package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ConversationTable : IntIdTable() {
    val createdAt = timestamp("created_at")
}