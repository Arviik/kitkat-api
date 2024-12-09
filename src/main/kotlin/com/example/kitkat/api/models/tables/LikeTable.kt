package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object LikeTable : IntIdTable() {
    val video = reference("video", VideoTable)
    val user = reference("user", UserTable)
}
