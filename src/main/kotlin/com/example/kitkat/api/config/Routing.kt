package com.example.kitkat.api.config

import com.example.kitkat.api.models.repository.CommentRepository
import com.example.kitkat.api.routes.configureCommentRoutes
import com.example.kitkat.api.routes.configureUserRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    configureUserRoutes()
    configureCommentRoutes(CommentRepository())
}
