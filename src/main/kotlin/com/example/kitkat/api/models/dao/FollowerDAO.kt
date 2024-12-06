package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Followers
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class FollowerDAO(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<FollowerDAO>(Followers)
}
