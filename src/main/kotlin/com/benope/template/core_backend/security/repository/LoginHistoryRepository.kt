package com.benope.template.core_backend.security.repository

import com.benope.template.core_backend.security.domain.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository

interface LoginHistoryRepository : JpaRepository<LoginHistory, Long> {
    fun findByUserId(userId: Long?): List<LoginHistory>
}