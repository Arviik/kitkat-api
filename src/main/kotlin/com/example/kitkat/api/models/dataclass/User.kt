package com.example.kitkat.api.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String,
    var email: String,
    var profilePictureUrl: String,
    var bio: String,
    var followersCount: Int,
    var followingCount: Int,
)
