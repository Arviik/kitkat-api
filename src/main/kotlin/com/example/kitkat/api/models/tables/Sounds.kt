package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Sounds : IntIdTable() {
    val title = varchar("title", 255)
    val author = reference("user", Users)
    val duration = integer("duration")
    val url = text("url")
    val usageCount = integer("usage_count")
}
