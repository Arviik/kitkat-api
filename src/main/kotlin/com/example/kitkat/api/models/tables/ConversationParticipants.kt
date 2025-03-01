package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.sql.Table

object ConversationParticipants: Table() {
    val conversation = reference("conversation", Conversations)
    val user = reference("user", Users)
    override val primaryKey = PrimaryKey(conversation, user)
}