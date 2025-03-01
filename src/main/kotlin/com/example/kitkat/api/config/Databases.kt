package com.example.kitkat.api.config

import com.example.kitkat.api.models.tables.*
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    transaction(Config.database) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            Comments, Followers,
            Likes,
            Notifications,
            SearchQueries, Sounds,
            Users, Videos,
            Conversations,
            ConversationParticipants,
            Messages
        )
    }
}
