package com.benope.verbose.spoon.core_backend.security.util

import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.security.core.context.SecurityContextHolder

fun getUserId(): Long? {
    val principal = SecurityContextHolder.getContext().authentication.principal

    return if (principal is User) {
        principal.userId
    } else {
        null
    }
}