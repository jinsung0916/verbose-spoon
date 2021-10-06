package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository
import com.benope.verbose.spoon.core_backend.security.domain.LoginHistory

interface LoginHistoryRepository : BaseRepository<LoginHistory, Long> {
    fun findByUserId(userId: Long?): List<LoginHistory>
}