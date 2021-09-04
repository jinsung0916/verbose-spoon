package com.benope.template.core_backend.security.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

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