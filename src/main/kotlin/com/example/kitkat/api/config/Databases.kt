package com.example.kitkat.api.config

import com.example.kitkat.api.models.tables.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val env = environment.config
    Database.connect(
        url = env.property("database.url").getString(),
        user = env.property("database.user").getString(),
        driver = env.property("database.driver").getString(),
        password = env.property("database.password").getString(),
    )

    transaction() {
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
