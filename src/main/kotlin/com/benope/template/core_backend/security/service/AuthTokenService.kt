package com.benope.template.core_backend.security.service

import com.benope.template.core_backend.security.domain.JwtToken
import org.springframework.security.core.userdetails.UserDetails

interface AuthTokenService {
    fun generateToken(userDetails: UserDetails?): JwtToken

    fun getUserDetailsFromToken(jwtToken: JwtToken?) : UserDetails
}