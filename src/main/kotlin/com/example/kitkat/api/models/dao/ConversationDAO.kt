package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.ConversationParticipants
import com.example.kitkat.api.models.tables.Conversations
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ConversationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConversationDAO>(Conversations)

    var participants by UserDAO via ConversationParticipants
    var createdAt by Conversations.createdAt
}