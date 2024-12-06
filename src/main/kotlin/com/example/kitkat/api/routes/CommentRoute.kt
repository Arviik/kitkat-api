package com.example.kitkat.api.routes

import com.example.kitkat.api.config.Config
import com.example.kitkat.api.models.dao.CommentDAO
import com.example.kitkat.api.services.CommentService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.configureCommentRoutes() {
    val commentService = CommentService(Config.database)
    routing {
        post("/comments") {
            val comment = call.receive<CommentDAO>()
            val id = commentService.create(comment)
            call.respond(HttpStatusCode.Created, id)
        }

        get("/comments/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid Id")
            val comment = commentService.read(id)
            if (comment != null) {
                call.respond(HttpStatusCode.OK, comment)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        put("/comments/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw java.lang.IllegalArgumentException("Invalid Id")
            val comment = call.receive<CommentDAO>()
            commentService.update(id, comment)
            call.respond(HttpStatusCode.OK)
        }

        delete("/comments/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw java.lang.IllegalArgumentException("Invalid Id")
            commentService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
