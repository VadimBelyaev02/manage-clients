package com.andersen.manageclients.config.security


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private var secret: String,
    @Value("\${jwt.expiration}") private val expiration: Long
) {

    @PostConstruct
    protected fun init() {
        secret = Base64.getEncoder().encodeToString(secret.toByteArray())
    }

    fun createToken(username: String, role: String): String {
        val now = LocalDateTime.now()
        var expirationTime = now.plusSeconds(expiration / 100)
        expirationTime = expirationTime.plusDays(10)
        val algorithm = Algorithm.HMAC256(secret)
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm)
    }

    fun getUsername(token: String): String {
        val decodedJWT: DecodedJWT = JWT.decode(token)
        return decodedJWT.subject
    }
}
