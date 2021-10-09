package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository
import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import com.benope.verbose.spoon.core_backend.security.domain.User

interface UserRepository : BaseRepository<User, Long> {

    fun findByUsername(username: String?): User?

    fun findByRefreshToken(refreshToken: JwtToken?): User?

    fun existsByUsername(username: String?): Boolean

}