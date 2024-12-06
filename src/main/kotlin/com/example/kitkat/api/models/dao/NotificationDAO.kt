package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Notifications
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class NotificationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationDAO>(Notifications)

    var user by UserDAO.Companion referencedOn Notifications.user
    var type by Notifications.type
    var relatedUser by UserDAO.Companion referencedOn Notifications.relatedUser
    var video by VideoDAO.Companion referencedOn Notifications.video
    var isRead by Notifications.isRead
    var createdAt by Notifications.createdAt
}
