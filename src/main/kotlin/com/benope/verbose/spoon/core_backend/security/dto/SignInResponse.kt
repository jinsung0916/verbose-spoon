package com.benope.verbose.spoon.core_backend.security.dto

import com.benope.verbose.spoon.core_backend.security.domain.JwtToken

data class SignInResponse(
    val authToken: JwtToken,
    val refreshToken: JwtToken
)