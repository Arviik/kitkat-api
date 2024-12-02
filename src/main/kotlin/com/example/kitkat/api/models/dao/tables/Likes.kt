package com.example.kitkat.api.models.dao.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Likes : IntIdTable() {
    val video = reference("video", Videos)
    val user = reference("user", Users)
}
