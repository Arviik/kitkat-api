package com.example.kitkat.api.routes

import com.example.kitkat.api.models.dataclass.Comment
import com.example.kitkat.api.models.repository.CommentRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCommentRoutes(repository: CommentRepository) {
    routing {
        route("/comments") {
            get {
                val comments = repository.all()
                call.respond(comments)
            }

            get("/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val comment = repository.byId(id.toInt())
                if (comment == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(comment)
            }

            post {
                try {
                    val comment = call.receive<Comment>()
                    repository.add(comment)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    println(ex.message)
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    println(ex.message)
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }


            }

            delete("/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                if (repository.remove(id.toInt())) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
