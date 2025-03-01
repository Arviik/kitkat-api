package com.example.kitkat.api.config

import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object Config {
    val dotenv = dotenv()

    const val SECRET = "your_secret_key"
    const val ISSUER = "http://0.0.0.0:8080/"
    const val AUDIENCE = "http://0.0.0.0:8080/hello"
    const val REALM = "kitkat-api"

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
