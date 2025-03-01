// ConversationRoutes.kt
package com.example.kitkat.api.routes

import com.example.kitkat.api.models.dataclass.ConversationDTO
import com.example.kitkat.api.models.repository.ConversationRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureConversationRoutes(repository: ConversationRepository) {
    routing {
        route("/conversations") {
            get {
                val conversations = repository.all()
                call.respond(conversations)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val conversation = repository.byId(id)
                if (conversation == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(conversation)
            }

            get("/user/{userId}") {
                val userId = call.parameters["userId"]?.toIntOrNull()
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val conversations = repository.getByUserId(userId)
                call.respond(conversations)
            }

            post {
                try {
                    val conversation = call.receive<ConversationDTO>()
                    repository.add(conversation)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message ?: "Invalid data")
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message ?: "Invalid JSON")
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }

                try {
                    val conversation = call.receive<ConversationDTO>()
                    val updated = repository.update(id, conversation)
                    if (updated) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } catch (ex: Exception) {
                    call.respond(HttpStatusCode.BadRequest, ex.message ?: "Invalid data")
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                if (repository.remove(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
