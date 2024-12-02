package com.example.kitkat.api.models.dao.entities

import com.example.kitkat.api.models.dao.tables.Notifications
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Notification(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Notification>(Notifications)

    var user by User.Companion referencedOn Notifications.user
    var type by Notifications.type
    var relatedUser by User.Companion referencedOn Notifications.relatedUser
    var video by Video.Companion referencedOn Notifications.video
    var isRead by Notifications.isRead
    var createdAt by Notifications.createdAt
}
