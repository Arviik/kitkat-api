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
    val dotenv = dotenv()
    Database.connect(
        url = dotenv["DATABASE_URL"],
        user = dotenv["DATABASE_USER"],
        driver = dotenv["DATABASE_DRIVER"],
        password = dotenv["DATABASE_PASSWORD"],
    )

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            CommentTable, FollowerTable, LikeTable, NotificationTable, SearchQueryTable, SoundTable, UserTable, VideoTable
        )
    }
}
