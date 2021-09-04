package com.benope.verbose.spoon.core_backend.security.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import javax.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank
    val username: String?,
    @field:NotBlank
    val password: String?
) {
    fun toAuthentication() : Authentication {
        return UsernamePasswordAuthenticationToken(username, password)
    }
}