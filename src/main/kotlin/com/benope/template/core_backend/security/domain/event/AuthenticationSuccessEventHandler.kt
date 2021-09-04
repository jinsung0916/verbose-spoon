package com.benope.template.core_backend.security.domain.event

import com.benope.template.core_backend.security.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AuthenticationSuccessEventHandler(
    private val loginAttemptService: LoginAttemptService,
    private val usernameExtractor: UsernameExtractor
) : ApplicationListener<AuthenticationSuccessEvent> {

    override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
        val authentication = event.source as Authentication
        val username = usernameExtractor.extract(authentication.principal)
        loginAttemptService.handleLoginSuccess(username)
    }

}