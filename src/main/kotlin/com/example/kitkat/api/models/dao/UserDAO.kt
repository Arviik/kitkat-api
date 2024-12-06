package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(Users)

    var name by Users.name
    var email by Users.email
    var passwordHash by Users.passwordHash
    var profilePictureUrl by Users.profilePictureUrl
    var bio by Users.bio
    var followers by UserDAO referencedOn Users.followers
    var followersCount by Users.followersCount
    var followingCount by Users.followingCount
}
