package com.example.kitkat.api.routes

import com.example.kitkat.api.config.Config
import com.example.kitkat.api.models.dataclass.CommentDTO
import com.example.kitkat.api.services.CommentService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureCommentRoutes() {
    val commentService = CommentService(Config.database)

    routing {
        post("/comments") {
            val commentDTO = call.receive<CommentDTO>()
            val id = commentService.create(commentDTO)

            val newComment = commentService.read(id)

            if (newComment != null) {
                call.respond(HttpStatusCode.Created, newComment)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Erreur lors de la récupération du commentaire")
            }
        }


        // 🔹 Récupérer les commentaires d'une vidéo avec les auteurs
        get("/comments/video/{videoId}") {
            val videoId = call.parameters["videoId"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid video ID")

            val comments = commentService.getCommentsWithAuthor(videoId)
            call.respond(HttpStatusCode.OK, comments)
        }


        // 🔹 Récupérer un commentaire par son ID
        get("/comments/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid comment ID")

            val comment = commentService.read(id)
            if (comment != null) {
                call.respond(HttpStatusCode.OK, comment)
            } else {
                call.respond(HttpStatusCode.NotFound, "Comment not found")
            }
        }

        // 🔹 Mettre à jour un commentaire
        put("/comments/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid comment ID")

            val commentDTO = call.receive<CommentDTO>()
            commentService.update(id, commentDTO)
            call.respond(HttpStatusCode.OK, "Comment updated successfully")
        }

        // 🔹 Supprimer un commentaire
        delete("/comments/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid comment ID")

            commentService.delete(id)
            call.respond(HttpStatusCode.OK, "Comment deleted successfully")
        }
    }
}
