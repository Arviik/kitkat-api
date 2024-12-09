package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.LikeTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LikeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LikeDAO>(LikeTable)

    var video by VideoDAO.Companion referencedOn LikeTable.video
    var user by UserDAO.Companion referencedOn LikeTable.user
}
