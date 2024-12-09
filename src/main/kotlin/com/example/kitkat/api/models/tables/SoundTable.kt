package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object SoundTable : IntIdTable() {
    val title = varchar("title", 255)
    val author = reference("user", UserTable)
    val duration = integer("duration")
    val url = text("url")
    val usageCount = integer("usage_count")
}
