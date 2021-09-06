package com.benope.verbose.spoon.core_backend.security.infra

import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.core_backend.security.exception.InvalidJwtTokenException
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.core_backend.security.service.RefreshTokenService
import com.benope.verbose.spoon.core_backend.security.util.doGenerateToken
import com.benope.verbose.spoon.core_backend.security.util.isTokenExpired
import io.jsonwebtoken.JwtException
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class UUIDRefreshTokenService(
    private val userRepository: UserRepository
) : RefreshTokenService {

    companion object {
        private const val JWT_TOKEN_VALIDITY = (5 * 60 * 60).toLong()
    }

    @Value("\${jwt.secret}")
    private lateinit var SECRET_KEY: String

    override fun generateToken(): JwtToken {
        return doGenerateToken(
            generateRandomUUIDString(),
            mutableMapOf(),
            Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000),
            SECRET_KEY
        )
    }

    private fun generateRandomUUIDString(): String {
        return UUID.randomUUID().toString()
    }

    override fun getUserDetailsFromToken(jwtToken: JwtToken?): UserDetails {
        try {
            return doGetUserDetailsFromToken(jwtToken)
        } catch (e: JwtException) {
            throw InvalidJwtTokenException().initCause(e)
        } catch (e: IllegalArgumentException) {
            throw InvalidJwtTokenException().initCause(e)
        }
    }

    private fun doGetUserDetailsFromToken(jwtToken: JwtToken?): UserDetails {
        if (jwtToken.isTokenExpired(SECRET_KEY)) {
            throw InvalidJwtTokenException()
        } else {
            return findUserByRefreshToken(jwtToken)
        }
    }

    private fun findUserByRefreshToken(refreshToken: JwtToken?): User {
        return userRepository.findByRefreshToken(refreshToken) ?: throw InvalidJwtTokenException()
    }
}