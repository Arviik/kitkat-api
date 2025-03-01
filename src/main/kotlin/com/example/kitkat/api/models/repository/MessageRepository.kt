package com.example.kitkat.api.models.repository

import com.example.kitkat.api.models.dao.MessageDAO
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dao.ConversationDAO
import com.example.kitkat.api.models.dataclass.Message
import com.example.kitkat.api.models.tables.Messages
import com.example.kitkat.api.services.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class MessageRepository : Repository<Message> {
    override suspend fun all(): List<Message> = suspendTransaction {
        MessageDAO.all().map(::daoToModel)
    }

    override suspend fun byId(id: Int): Message? = suspendTransaction {
        MessageDAO.find { Messages.id eq id }.firstOrNull()?.let(::daoToModel)
    }

    override suspend fun add(model: Message) {
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

    override suspend fun update(id: Int, model: Message): Boolean = suspendTransaction {
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

    private fun daoToModel(dao: MessageDAO) = Message(
        senderId = dao.sender.id.value,
        receiverId = dao.receiver.id.value,
        content = dao.content,
        createdAt = dao.createdAt.toString(),
        conversationId = dao.conversation.id.value,
        isSystemMessage = dao.isSystemMessage
    )
}
