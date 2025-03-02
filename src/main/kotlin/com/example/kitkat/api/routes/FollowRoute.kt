package com.example.kitkat.api.routes
import com.example.kitkat.api.services.UserService
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.respond

fun Application.configureFollowRoutes() {
    val userService = UserService()

    routing {
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

        post("/users/{id}/unfollow") {
            val userId = call.parameters["id"]?.toIntOrNull()
            val followerId = call.receive<Int>()
            if (userId != null && userId != followerId) {
                userService.removeFollower(userId, followerId)
                call.respond(HttpStatusCode.OK, "User $followerId is no longer following $userId.")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID or trying to unfollow oneself.")
            }
        }

        get("/users/{id}/is-following/{followerId}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            val followerId = call.parameters["followerId"]?.toIntOrNull()

            if (userId == null || followerId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user IDs.")
                return@get
            }

            val isFollowing = userService.isFollowing(userId, followerId)

            call.respond(HttpStatusCode.OK, mapOf("isFollowing" to isFollowing))
        }


        // Liste des abonnés
        get("/users/{id}/followers") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId != null) {
                val followers = userService.listFollowers(userId)
                call.respond(HttpStatusCode.OK, followers)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            }
        }

        // Liste des utilisateurs suivis
        get("/users/{id}/following") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId != null) {
                val following = userService.getFollowing(userId)
                call.respond(HttpStatusCode.OK, following)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            }
        }
    }
}
