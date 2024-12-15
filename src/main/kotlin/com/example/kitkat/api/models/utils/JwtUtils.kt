package com.example.kitkat.api.models.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.kitkat.api.models.dao.UserDAO
import java.util.Date

fun generateJWT(userId: UserDAO?, email: String): String {
    val jwtIssuer = "kitkat-api"
    val jwtAudience = "kitkat-audience"
    val secret = "your_secret_key"

    return JWT.create()
        .withIssuer(jwtIssuer)
        .withAudience(jwtAudience)
        .withClaim("email", email)
        .withIssuedAt(Date())
        .withExpiresAt(Date(System.currentTimeMillis() + 3600 * 1000)) // 1 heure
        .sign(Algorithm.HMAC256(secret))
}
