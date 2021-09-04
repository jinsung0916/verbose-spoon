package com.benope.template.core_backend.security.dto

import com.benope.template.core_backend.security.domain.JwtToken

data class RefreshRequest(
    val refreshToken: JwtToken?
)