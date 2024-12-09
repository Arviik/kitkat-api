package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable() {
    val name = varchar("name", 255)
    val email = varchar("email", 255).index()
    val passwordHash = text("password_hash")
    val profilePictureUrl = text("profile_picture_url")
    val bio = text("bio")
    val followers = reference("followers", UserTable)
    val followersCount = integer("followers_count")
    val followingCount = integer("following_count")
}
