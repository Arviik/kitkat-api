package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Likes
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LikeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LikeDAO>(Likes)

    var video by VideoDAO.Companion referencedOn Likes.video
    var user by UserDAO.Companion referencedOn Likes.user
}
