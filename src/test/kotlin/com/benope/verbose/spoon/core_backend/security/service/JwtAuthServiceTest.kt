package com.benope.verbose.spoon.core_backend.security.service

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import com.benope.verbose.spoon.core_backend.security.dto.RefreshRequest
import com.benope.verbose.spoon.core_backend.security.dto.SignInRequest
import com.benope.verbose.spoon.core_backend.security.exception.InvalidJwtTokenException
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException

internal class JwtAuthServiceTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val authService: JwtAuthService,
    @Autowired private val refreshTokenService: RefreshTokenService
) : BenopeTest() {

    @Test
    @DisplayName("로그인 성공 시 JWT 토큰을 반환한다.")
    fun signInReturnJwtTokenTest() {
        // Given
        val signInRequest = SignInRequest(USERNAME, PASSWORD)

        // When
        val signInResponse = authService.signIn(signInRequest)

        // Then
        assertThat(signInResponse.authToken.value).isNotEmpty
        assertThat(signInResponse.refreshToken.value).isNotEmpty
    }

    @Test
    @DisplayName("로그인 성공 시 refresh token 을 DB에 저장한다.")
    fun signInSaveRefreshTokenTest() {
        // Given
        val signInRequest = SignInRequest(USERNAME, PASSWORD)

        // When
        val signInResponse = authService.signIn(signInRequest)

        // Then
        val user = userRepository.findByUsername(USERNAME)
        assertThat(
            user?.isValidRefreshToken(signInResponse.refreshToken)
        ).isTrue
    }

    @Test
    @DisplayName("로그인 실패 시 예외 처리한다.")
    fun signInFailureTest() {
        // Given
        val username = ""
        val password = ""
        val signInRequest = SignInRequest(username, password)

        // When

        // Then
        assertThrows<AuthenticationException> { authService.signIn(signInRequest) }
    }

    @Test
    @DisplayName("Refresh 토큰이 유효할 경우, 새로운 auth 토큰을 반환한다.")
    fun refreshTest() {
        // Given
        val signInRequest = SignInRequest(USERNAME, PASSWORD)
        val signInResponse = authService.signIn(signInRequest)
        val refreshRequest = RefreshRequest(signInResponse.refreshToken)

        // When
        val refreshResponse = authService.refresh(refreshRequest)

        // Then
        assertThat(refreshResponse.authToken.value).isNotEmpty
    }

    @Test
    @DisplayName("Refresh 토큰이 유효하지 않을 경우, 예외를 발생시킨다(1).")
    fun refreshFailureTest1() {
        // Given
        val refreshRequest = RefreshRequest(JwtToken(""))

        // When

        // Then
        assertThrows<InvalidJwtTokenException> { authService.refresh(refreshRequest) }
    }

    @Test
    @DisplayName("Refresh 토큰이 유효하지 않을 경우, 예외를 발생시킨다(2).")
    fun refreshFailureTest2() {
        // Given
        val refreshRequest = RefreshRequest(refreshTokenService.generateToken())

        // When

        // Then
        assertThrows<InvalidJwtTokenException> { authService.refresh(refreshRequest) }
    }

}