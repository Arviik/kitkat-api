package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Messages
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MessageDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageDAO>(Messages)

    var sender by UserDAO referencedOn Messages.sender
    var receiver by UserDAO referencedOn Messages.receiver
    var content by Messages.content
    var createdAt by Messages.createdAt
    var conversation by ConversationDAO referencedOn Messages.conversation
    var isSystemMessage by Messages.isSystemMessage
}