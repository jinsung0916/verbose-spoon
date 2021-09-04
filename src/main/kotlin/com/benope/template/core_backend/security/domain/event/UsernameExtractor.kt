package com.benope.template.core_backend.security.domain.event

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class UsernameExtractor {

    fun extract(principal: Any?): String? {
        return when (principal) {
            is UserDetails -> {
                principal.username
            }
            is String -> {
                principal
            }
            else -> {
                null
            }
        }
    }

}