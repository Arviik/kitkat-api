package com.example.kitkat.api.models.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.kitkat.api.config.Config
import com.example.kitkat.api.models.dao.UserDAO
import java.util.Date

fun generateJWT(user: UserDAO): String {
    return JWT.create()
        .withAudience(Config.AUDIENCE)
        .withIssuer(Config.ISSUER)
        .withClaim("username", user.name)
        .withClaim("email", user.email)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600 * 1000))
        .sign(Algorithm.HMAC256(Config.SECRET))
}
