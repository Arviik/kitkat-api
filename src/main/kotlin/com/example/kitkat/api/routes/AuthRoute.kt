package com.example.kitkat.api.routes
import com.example.kitkat.api.services.UserService
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.kitkat.api.config.Config
import com.example.kitkat.api.models.dataclass.LoginRequestDTO
import com.example.kitkat.api.models.dataclass.UserDTO
import com.example.kitkat.api.models.dataclass.UserWithoutPasswordDTO
import com.example.kitkat.api.models.utils.generateJWT
import io.ktor.server.response.respond


fun Application.configureAuthRoutes() {
    val userService = UserService(Config.database)

    routing {
        post("/auth/register") {
            val UserDTO = call.receive<UserDTO>()
            val id = userService.create(UserDTO)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        post("/auth/login") {
            val loginRequest = call.receive<LoginRequestDTO>()
            val user = userService.authenticate(loginRequest.email, loginRequest.password)
            if (user != null) {
                val token = generateJWT(user, user.email) // Génère un JWT pour l'utilisateur connecté
                call.respond(HttpStatusCode.OK, mapOf("token" to token, "message" to "Login successful"))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
            }
        }

        post("/auth/logout") {
            call.respond(HttpStatusCode.OK, "Logout successful")
        }
    }
}
