package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SearchQueryTable : IntIdTable() {
    val user = reference("user", UserTable)
    val query = text("query")
    val createdAt = timestamp("created_at")
}
