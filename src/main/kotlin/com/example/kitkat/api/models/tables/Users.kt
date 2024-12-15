package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val passwordHash = text("password_hash")
    val profilePictureUrl = text("profile_picture_url").nullable()
    val bio = text("bio").nullable()
    val followersCount = integer("followers_count").default(0)
    val followingCount = integer("following_count").default(0)
}
