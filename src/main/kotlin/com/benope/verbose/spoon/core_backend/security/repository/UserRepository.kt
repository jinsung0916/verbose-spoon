package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {

    @Query("SELECT u from User u where u.isDeleted = false")
    override fun findAll(pageable: Pageable): Page<User>

    @Query("SELECT u from User u where u.username = :username AND u.isDeleted = false")
    fun findByUsername(username: String?): User?

    @Query("SELECT u from User u where u.refreshToken = :refreshToken AND u.isDeleted = false")
    fun findByRefreshToken(refreshToken: JwtToken?): User?

    fun existsByUsername(username: String?): Boolean

}