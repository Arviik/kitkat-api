package com.example.kitkat.api

import com.example.kitkat.api.config.AppConfig
import com.example.kitkat.api.config.configureDatabases
import com.example.kitkat.api.config.configureRouting
import com.example.kitkat.api.config.configureSecurity
import com.example.kitkat.api.config.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    AppConfig.application = this

    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
