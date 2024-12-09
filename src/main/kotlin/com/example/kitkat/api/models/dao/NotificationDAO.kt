package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.NotificationTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class NotificationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationDAO>(NotificationTable)

    var user by UserDAO.Companion referencedOn NotificationTable.user
    var type by NotificationTable.type
    var relatedUser by UserDAO.Companion referencedOn NotificationTable.relatedUser
    var video by VideoDAO.Companion referencedOn NotificationTable.video
    var isRead by NotificationTable.isRead
    var createdAt by NotificationTable.createdAt
}
