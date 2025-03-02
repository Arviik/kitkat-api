package com.example.kitkat.api.routes

import com.example.kitkat.api.models.dataclass.MessageDTO
import com.example.kitkat.api.models.repository.MessageRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

val activeConnections = Collections.synchronizedSet<DefaultWebSocketServerSession?>(mutableSetOf())

fun Application.configureMessageRoutes(repository: MessageRepository) {
    routing {
        route("/messages") {
            get {
                val messages = repository.all()
                call.respond(messages)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val message = repository.byId(id)
                if (message == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(message)
            }

            get("conversation/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val message = repository.byConversationId(id)
                call.respond(message)
            }

            post {
                try {
                    val message = call.receive<MessageDTO>()
                    repository.add(message)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    println(ex.message)
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    println(ex.message)
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }

                try {
                    val message = call.receive<MessageDTO>()
                    val updated = repository.update(id, message)
                    if (updated) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond(HttpStatusCode.BadRequest)
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

            webSocket("/ws/conversation/{conversationId}") {
                val conversationId = call.parameters["conversationId"]?.toIntOrNull()
                if (conversationId == null) {
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid conversation ID"))
                    return@webSocket
                }

                activeConnections += this
                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val receivedText = frame.readText()
                            val message = Json.decodeFromString<MessageDTO>(receivedText)
                            repository.add(message)

                            // Envoie du message à tous les clients connectés
                            val jsonMessage = Json.encodeToString(message)
                            activeConnections.forEach { session ->
                                session?.send(Frame.Text(jsonMessage))
                            }
                        }
                    }
                } finally {
                    activeConnections -= this
                }
            }
        }
    }
}