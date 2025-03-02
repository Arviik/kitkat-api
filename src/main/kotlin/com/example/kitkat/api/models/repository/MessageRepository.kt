package com.example.kitkat.api.models.repository

import com.example.kitkat.api.models.dao.MessageDAO
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dao.ConversationDAO
import com.example.kitkat.api.models.dataclass.MessageDTO
import com.example.kitkat.api.models.tables.Messages
import com.example.kitkat.api.services.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll

class MessageRepository : Repository<MessageDTO> {
    override suspend fun all(): List<MessageDTO> = suspendTransaction {
        MessageDAO.all().map(::daoToModel)
    }

    override suspend fun byId(id: Int): MessageDTO? = suspendTransaction {
        MessageDAO.find { Messages.id eq id }.firstOrNull()?.let(::daoToModel)
    }

    suspend fun byConversationId(conversationId: Int): List<MessageDTO> = suspendTransaction {
        Messages
            .selectAll()
            .where { Messages.conversation eq conversationId }
            .map {
                MessageDTO(
                    senderId = it[Messages.sender].value,
                    receiverId = it[Messages.receiver].value,
                    content = it[Messages.content],
                    createdAt = it[Messages.createdAt].toString(),
                    conversationId = it[Messages.conversation].value,
                    isSystemMessage = it[Messages.isSystemMessage],
                )
            }
    }

    override suspend fun add(model: MessageDTO) {
        val sender = UserDAO.findById(model.senderId) ?: throw IllegalArgumentException("Sender not found")
        val receiver = UserDAO.findById(model.receiverId) ?: throw IllegalArgumentException("Receiver not found")
        val conversation = ConversationDAO.findById(model.conversationId) ?: throw IllegalArgumentException("Conversation not found")

        MessageDAO.new {
            this.sender = sender
            this.receiver = receiver
            this.content = model.content
            this.createdAt = Instant.parse(model.createdAt)
            this.conversation = conversation
            this.isSystemMessage = model.isSystemMessage
        }
    }

    override suspend fun update(id: Int, model: MessageDTO): Boolean = suspendTransaction {
        val message = MessageDAO.findById(id) ?: return@suspendTransaction false
        val sender = UserDAO.findById(model.senderId) ?: throw IllegalArgumentException("Sender not found")
        val receiver = UserDAO.findById(model.receiverId) ?: throw IllegalArgumentException("Receiver not found")
        val conversation = ConversationDAO.findById(model.conversationId) ?: throw IllegalArgumentException("Conversation not found")

        message.apply {
            this.sender = sender
            this.receiver = receiver
            this.content = model.content
            this.createdAt = Instant.parse(model.createdAt)
            this.conversation = conversation
            this.isSystemMessage = model.isSystemMessage
        }
        true
    }

    override suspend fun remove(id: Int): Boolean = suspendTransaction {
        Messages.deleteWhere { Messages.id eq id } > 0
    }

    private fun daoToModel(dao: MessageDAO) = MessageDTO(
        senderId = dao.sender.id.value,
        receiverId = dao.receiver.id.value,
        content = dao.content,
        createdAt = dao.createdAt.toString(),
        conversationId = dao.conversation.id.value,
        isSystemMessage = dao.isSystemMessage
    )
}
