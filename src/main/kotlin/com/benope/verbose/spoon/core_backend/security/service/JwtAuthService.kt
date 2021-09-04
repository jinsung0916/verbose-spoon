package com.benope.verbose.spoon.core_backend.security.service

import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.core_backend.security.dto.RefreshRequest
import com.benope.verbose.spoon.core_backend.security.dto.RefreshResponse
import com.benope.verbose.spoon.core_backend.security.dto.SignInRequest
import com.benope.verbose.spoon.core_backend.security.dto.SignInResponse
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class JwtAuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val refreshTokenService: RefreshTokenService
) {

    @Transactional
    fun signIn(signInRequest: SignInRequest): SignInResponse {
        val authenticated = authenticationManager.authenticate(signInRequest.toAuthentication())
        val signInResponse = getTokens(authenticated)

        val user = authenticated.principal as User
        user.refreshToken = signInResponse.refreshToken
        userRepository.save(user)

        return signInResponse
    }

    private fun getTokens(authentication: Authentication): SignInResponse {
        val user = authentication.principal as UserDetails
        val authToken = authTokenService.generateToken(user)
        val refreshToken = refreshTokenService.generateToken()
        return SignInResponse(authToken, refreshToken)
    }

    fun refresh(refreshRequest: RefreshRequest): RefreshResponse {
        val user = refreshTokenService.getUserDetailsFromToken(refreshRequest.refreshToken)
        val authToken = authTokenService.generateToken(user)
        return RefreshResponse(authToken)
    }
}