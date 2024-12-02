package com.example.kitkat.api.models.dao.entities

import com.example.kitkat.api.models.dao.tables.Followers
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class Follower(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<Follower>(Followers)
}
