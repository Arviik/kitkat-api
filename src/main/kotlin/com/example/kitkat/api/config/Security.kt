package com.example.kitkat.api.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond

fun Application.configureSecurity() {
    val env = environment.config

    install(Authentication) {
        jwt("auth-jwt") {
            realm = env.property("jwt.realm").getString()
            verifier(JWT
                .require(Algorithm.HMAC256(env.property("jwt.secret").getString()))
                .withAudience(env.property("jwt.audience").getString())
                .withIssuer(env.property("jwt.issuer").getString())
                .build())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
