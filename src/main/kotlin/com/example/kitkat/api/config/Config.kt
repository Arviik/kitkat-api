package com.example.kitkat.api.config

import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object Config {
    val dotenv = dotenv()
    val database by lazy {
        Database.connect(
            url = dotenv["DATABASE_URL"],
            user = dotenv["DATABASE_USER"],
            driver = dotenv["DATABASE_DRIVER"],
            password = dotenv["DATABASE_PASSWORD"],
        )
    }
}
