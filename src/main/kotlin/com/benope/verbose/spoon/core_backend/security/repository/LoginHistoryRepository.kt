package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.security.domain.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository

interface LoginHistoryRepository : JpaRepository<LoginHistory, Long> {
    fun findByUserId(userId: Long?): List<LoginHistory>
}