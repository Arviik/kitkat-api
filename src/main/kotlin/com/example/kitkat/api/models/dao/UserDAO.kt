package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.dataclass.UserWithoutPasswordDTO
import com.example.kitkat.api.models.tables.UserFollowers
import com.example.kitkat.api.models.tables.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(Users)

    var name by Users.name
    var email by Users.email
    var passwordHash by Users.passwordHash
    var profilePictureUrl by Users.profilePictureUrl
    var bio by Users.bio
    var followersCount by Users.followersCount
    var followingCount by Users.followingCount
    val followers by UserDAO.via(UserFollowers.follower, UserFollowers.user)
    val following by UserDAO.via(UserFollowers.user, UserFollowers.follower)
    fun toUserWithoutPasswordDTO(): UserWithoutPasswordDTO {
        return UserWithoutPasswordDTO(
            id = this.id.value,
            name = this.name,
            email = this.email,
            profilePictureUrl = this.profilePictureUrl,
            bio = this.bio,
            followersCount = this.followersCount,
            followingCount = this.followingCount
        )
    }
}