package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.sql.Table

object ConversationParticipantTable : Table() {
    val conversation = reference("conversation", ConversationTable)
    val user = reference("user", UserTable)
    override val primaryKey = PrimaryKey(conversation, user)
}