package com.benope.template.core_backend.security.controller

import com.benope.template.core_backend.common.exception.DtoValidationException
import com.benope.template.core_backend.security.dto.RefreshRequest
import com.benope.template.core_backend.security.dto.RefreshResponse
import com.benope.template.core_backend.security.dto.SignInRequest
import com.benope.template.core_backend.security.dto.SignInResponse
import com.benope.template.core_backend.security.service.JwtAuthService
import org.springframework.context.annotation.Profile
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Profile("api")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val jwtAuthService: JwtAuthService
) {

    @PostMapping("/signIn")
    fun signIn(@Valid @RequestBody signInRequest: SignInRequest, errors: BindingResult): SignInResponse {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        return jwtAuthService.signIn(signInRequest)
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody refreshRequest: RefreshRequest, errors: BindingResult): RefreshResponse {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        return jwtAuthService.refresh(refreshRequest)
    }

}