package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var profilePictureUrl by UserTable.profilePictureUrl
    var bio by UserTable.bio
    var followers by UserDAO referencedOn UserTable.followers
    var followersCount by UserTable.followersCount
    var followingCount by UserTable.followingCount
}
