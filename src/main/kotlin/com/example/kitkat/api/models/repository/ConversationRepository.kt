// ConversationRepository.kt
package com.example.kitkat.api.models.repository

import com.example.kitkat.api.models.dao.ConversationDAO
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dataclass.ConversationDTO
import com.example.kitkat.api.models.tables.ConversationParticipants
import com.example.kitkat.api.models.tables.Conversations
import com.example.kitkat.api.services.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SizedCollection

class ConversationRepository : Repository<ConversationDTO> {
    override suspend fun all(): List<ConversationDTO> = suspendTransaction {
        ConversationDAO.all().map(::daoToModel)
    }

    override suspend fun byId(id: Int): ConversationDTO? = suspendTransaction {
        ConversationDAO
            .find { (Conversations.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    suspend fun getByUserId(userId: Int): List<ConversationDTO> = suspendTransaction {
        ConversationDAO.find {
            ConversationParticipants.user eq userId
        }.map(::daoToModel)
    }

    override suspend fun add(model: ConversationDTO) {
        val participants = model.participantIds.map {
            UserDAO.findById(it) ?: throw IllegalArgumentException("User not found")
        }

        ConversationDAO.new {
            this.createdAt = Instant.parse(model.createdAt)
            this.participants = SizedCollection(participants)
        }
    }

    override suspend fun update(id: Int, model: ConversationDTO): Boolean = suspendTransaction {
        val participants = model.participantIds.map {
            UserDAO.findById(it) ?: throw IllegalArgumentException("User not found")
        }

        val rowsUpdated = ConversationDAO.findByIdAndUpdate(id) {
            it.createdAt = Instant.parse(model.createdAt)
            it.participants = SizedCollection(participants)
        }
        rowsUpdated != null
    }

    override suspend fun remove(id: Int): Boolean = suspendTransaction {
        ConversationParticipants.deleteWhere { ConversationParticipants.conversation eq id }
        val rowsDeleted = Conversations.deleteWhere {
            Conversations.id eq id
        }
        rowsDeleted == 1
    }

    fun daoToModel(dao: ConversationDAO) = ConversationDTO(
        dao.id.value,
        dao.participants.map { it.id.value },
        dao.createdAt.toString()
    )
}
