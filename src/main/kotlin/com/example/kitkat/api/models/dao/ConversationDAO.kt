package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.ConversationParticipantTable
import com.example.kitkat.api.models.tables.ConversationTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ConversationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConversationDAO>(ConversationTable)

    var participants by UserDAO via ConversationParticipantTable
    var createdAt by ConversationTable.createdAt
}