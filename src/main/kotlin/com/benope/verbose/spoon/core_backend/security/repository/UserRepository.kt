package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String?): User?

    fun findByRefreshToken(refreshToken: JwtToken?): User?
}