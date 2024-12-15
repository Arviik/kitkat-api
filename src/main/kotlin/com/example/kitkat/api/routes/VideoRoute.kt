package com.example.kitkat.api.routes

import com.example.kitkat.api.models.dao.VideoDAO
import com.example.kitkat.api.services.VideoService
import com.example.kitkat.api.config.Config
import com.example.kitkat.api.models.dataclass.VideoDTO
import com.example.kitkat.api.services.UserService
import io.ktor.server.application.Application
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
fun Application.configureVideoRoutes() {
    val videoService = VideoService(Config.database)

    routing {
        post("/videos") {
            val videoDTO = call.receive<VideoDTO>()
            val id = videoService.create(videoDTO)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
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
