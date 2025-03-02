package com.example.kitkat.api.models.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.kitkat.api.config.AppConfig
import com.example.kitkat.api.models.dao.UserDAO
import java.util.Date

fun generateJWT(user: UserDAO): String {
    val env = AppConfig.application.environment.config

    return JWT.create()
        .withAudience(env.property("jwt.audience").getString())
        .withIssuer(env.property("jwt.issuer").getString())
        .withClaim("id", user.id.value)
        .withClaim("username", user.name)
        .withClaim("email", user.email)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600 * 1000))
        .sign(Algorithm.HMAC256(env.property("jwt.secret").getString()))
}
