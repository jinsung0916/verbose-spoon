package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    override fun findAll(pageable: Pageable): Page<User>

    override fun findById(userId: Long): Optional<User>

    fun findByUsername(username: String?): User?

    fun findByRefreshToken(refreshToken: JwtToken?): User?

    fun existsByUsername(username: String?): Boolean

}