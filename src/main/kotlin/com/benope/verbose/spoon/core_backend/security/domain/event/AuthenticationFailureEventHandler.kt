package com.benope.verbose.spoon.core_backend.security.domain.event

import com.benope.verbose.spoon.core_backend.security.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureEventHandler(
    private val loginAttemptService: LoginAttemptService,
    private val usernameExtractor: UsernameExtractor
) : ApplicationListener<AbstractAuthenticationFailureEvent> {

    override fun onApplicationEvent(event: AbstractAuthenticationFailureEvent) {
        val authentication = event.source as Authentication
        val username = usernameExtractor.extract(authentication.principal)
        loginAttemptService.handleLoginFailure(username)
    }

}