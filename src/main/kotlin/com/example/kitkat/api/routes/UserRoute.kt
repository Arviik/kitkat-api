package com.example.kitkat.api.routes

import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.services.UserService
import com.example.kitkat.api.models.dataclass.UserDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureUserRoutes() {
    val userService = UserService()

    routing {
        post("/users") {
            val userDTO = call.receive<UserDTO>()
            val id = userService.create(userDTO)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        get("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        authenticate("auth-jwt") {

        put("/users/update-profile-picture") {
            val principal = call.principal<JWTPrincipal>()
                ?: return@put call.respond(HttpStatusCode.Unauthorized, "Token invalide")

            val userId = principal.payload.getClaim("id").asInt()
            val imageUrl = call.receive<String>()

            var success = false

            transaction {
                val user = UserDAO.findById(userId)

                if (user != null) {
                    user.profilePictureUrl = imageUrl.trim('"')
                    success = true
                }
            }

            if (success) {
                call.respond(HttpStatusCode.OK, "Image mise à jour avec succès")
            } else {
                call.respond(HttpStatusCode.NotFound, "Utilisateur introuvable")
            }
        }
        }


        put("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val userDTO = call.receive<UserDTO>()
            userService.update(id, userDTO)
            call.respond(HttpStatusCode.OK)
        }

        delete("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
        post("/users/{id}/follow") {
            val userId = call.parameters["id"]?.toIntOrNull()
            val followerId = call.receive<Int>()
            if (userId != null && userId != followerId) {
                userService.followUser(userId, followerId)
                call.respond(HttpStatusCode.OK, "User $followerId is now following $userId.")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID or trying to follow oneself.")
            }
        }

    }
}