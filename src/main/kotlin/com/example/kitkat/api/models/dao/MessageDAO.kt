package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.MessageTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MessageDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageDAO>(MessageTable)

    var sender by UserDAO referencedOn MessageTable.sender
    var receiver by UserDAO referencedOn MessageTable.receiver
    var content by MessageTable.content
    var createdAt by MessageTable.createdAt
    var conversation by ConversationDAO referencedOn MessageTable.conversation
    var isSystemMessage by MessageTable.isSystemMessage
}