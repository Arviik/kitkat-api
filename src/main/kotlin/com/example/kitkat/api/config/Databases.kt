package com.example.kitkat.api.config

import com.example.kitkat.api.models.tables.Comments
import com.example.kitkat.api.models.tables.Followers
import com.example.kitkat.api.models.tables.Likes
import com.example.kitkat.api.models.tables.Notifications
import com.example.kitkat.api.models.tables.SearchQueries
import com.example.kitkat.api.models.tables.Sounds
import com.example.kitkat.api.models.tables.Users
import com.example.kitkat.api.models.tables.Videos
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    transaction(Config.database) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.createMissingTablesAndColumns(
            Comments, Followers, Likes, Notifications, SearchQueries, Sounds, Users, Videos
        )
    }
}
