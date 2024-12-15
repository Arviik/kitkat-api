package com.example.kitkat.api.config

import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object Config {
    val dotenv = dotenv()
    val database by lazy {
        Database.connect(
            url = "jdbc:postgresql://database:5432/kitkat",
            driver = "org.postgresql.Driver",
            user = "kitkat",
            password = "kitkat"
        ).also {
            println("Database connected successfully: ${it.url}")
        }
    }
}
