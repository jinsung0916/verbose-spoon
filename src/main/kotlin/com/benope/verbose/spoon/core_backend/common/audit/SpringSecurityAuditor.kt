package com.benope.verbose.spoon.core_backend.common.audit

import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringSecurityAuditor : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (isValidAuthentication(authentication)) {
            val principal = authentication.principal as User
            Optional.of(principal.userId!!)
        } else {
            Optional.empty()
        }
    }

    private fun isValidAuthentication(authentication: Authentication?): Boolean {
        return !Objects.isNull(authentication) && authentication!!.isAuthenticated
    }
}