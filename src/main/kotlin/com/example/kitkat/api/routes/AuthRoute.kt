package com.example.kitkat.api.routes
import com.example.kitkat.api.services.UserService
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.kitkat.api.config.Config
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dataclass.LoginRequestDTO
import com.example.kitkat.api.models.dataclass.UserDTO
import com.example.kitkat.api.models.utils.generateJWT
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureAuthRoutes() {
    val userService = UserService(Config.database)

    routing {
        post("/auth/register") {
            val userDTO = call.receive<UserDTO>()
            val id = userService.create(userDTO)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        post("/auth/login") {
            val loginRequest = call.receive<LoginRequestDTO>()
            val user = userService.authenticate(loginRequest.email, loginRequest.password)
            if (user != null) {
                val token = generateJWT(user) // Génère un JWT pour l'utilisateur connecté
                call.respond(HttpStatusCode.OK, mapOf("token" to token, "message" to "Login successful"))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
            }
        }

        post("/auth/logout") {
            call.respond(HttpStatusCode.OK, "Logout successful")
        }
        authenticate("auth-jwt") {
            get("/auth/me") {
                val principal = call.principal<JWTPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized, "Token invalide")

                val userId = principal.payload.getClaim("id").asInt()

                val user = transaction { UserDAO.findById(userId)?.toUserWithoutPasswordDTO() }

                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Utilisateur introuvable")
                }
            }

            get("/auth/token-tester") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis()).toString()
                call.respond(HttpStatusCode.OK, mapOf("username" to username, "expiresAt" to expiresAt))
            }
        }
    }
}
