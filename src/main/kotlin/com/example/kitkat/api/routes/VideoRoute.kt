package com.example.kitkat.api.routes

import com.example.kitkat.api.models.dao.VideoDAO
import com.example.kitkat.api.models.dataclass.UserWithoutPasswordDTO
import com.example.kitkat.api.services.VideoService
import com.example.kitkat.api.models.dataclass.VideoDTO
import com.example.kitkat.api.models.tables.Followers
import com.example.kitkat.api.models.tables.Users
import com.example.kitkat.api.models.tables.Videos
import io.ktor.server.application.Application
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureVideoRoutes() {
    val videoService = VideoService()

    routing {
        post("/videos") {
            val videoDTO = call.receive<VideoDTO>()
            val id = videoService.create(videoDTO)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        get("/videos-with-authors") {
            val videosWithAuthors = videoService.getAllVideosWithAuthors()
            call.respond(HttpStatusCode.OK, videosWithAuthors)
        }
        authenticate("auth-jwt") {
            get("/videos/friends") {
                val principal = call.principal<JWTPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized, "Token invalide")

                val userId = principal.payload.getClaim("id").asInt()

                val followedUsers = transaction {
                    Followers.selectAll().where { Followers.follower eq userId }
                        .map { it[Followers.followed] }
                }

                if (followedUsers.isNotEmpty()) {
                    val videosWithAuthors = transaction {
                        Videos.innerJoin(Users)
                            .selectAll().where { Videos.author inList followedUsers }
                            .map { row ->
                                val video = VideoDTO(
                                    id = row[Videos.id].value,
                                    title = row[Videos.title],
                                    duration = row[Videos.duration],
                                    authorId = row[Videos.author].value,
                                    videoUrl = row[Videos.videoUrl],
                                    thumbnailUrl = row[Videos.thumbnailUrl],
                                    viewCount = row[Videos.viewCount],
                                    likeCount = row[Videos.likeCount],
                                    commentCount = row[Videos.commentCount],
                                    createdAt = row[Videos.createdAt].toString(),
                                    isPublic = row[Videos.isPublic]
                                )

                                val user = UserWithoutPasswordDTO(
                                    id = row[Users.id].value,
                                    name = row[Users.name],
                                    email = row[Users.email],
                                    profilePictureUrl = row[Users.profilePictureUrl],
                                    bio = row[Users.bio],
                                    followersCount = row[Users.followersCount],
                                    followingCount = row[Users.followingCount]
                                )

                                video to user
                            }
                    }

                    call.respond(HttpStatusCode.OK, videosWithAuthors)
                } else {
                    call.respond(HttpStatusCode.OK, emptyList<VideoDTO>())
                }
            }
        }





        get("/videos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val video = id?.let { videoService.read(it) }
            if (video != null) {
                call.respond(HttpStatusCode.OK, video)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get("/videos/author/{authorId}") {
            val authorId = call.parameters["authorId"]?.toIntOrNull()
            if (authorId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid author ID")
            } else {
                val videos = videoService.getVideosByAuthor(authorId)
                call.respond(HttpStatusCode.OK, videos)
            }
        }

        put("/videos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val videoDTO = call.receive<VideoDTO>()
            if (id != null) {
                videoService.update(id, videoDTO)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/videos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                videoService.delete(id)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
