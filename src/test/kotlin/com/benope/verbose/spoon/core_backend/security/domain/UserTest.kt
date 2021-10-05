package com.benope.verbose.spoon.core_backend.security.domain

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.repository.LoginHistoryRepository
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException

internal class UserTest : BenopeTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var loginHistoryRepository: LoginHistoryRepository

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Test
    @DisplayName("계정 잠금 이후 인증 시도 시 예외가 발생한다.")
    fun authenticateUserAfterAccountLockedTest() {
        // Given
        val user = userRepository.findByUsername(USERNAME)
        makeUserInvalid(user)
        userRepository.save(user!!)

        // When
        val token = UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)

        // Then
        assertThrows<LockedException> { authenticationManager.authenticate(token) }
    }

    @Test
    @DisplayName("로그인 성공 이후 성공 이력을 저장한다.")
    fun saveUserHistoryAfterLoginSuccessTest() {
        // Given
        val token = UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)

        // When
        val user = authenticationManager.authenticate(token).principal as User

        // Then
        val loginHistories = loginHistoryRepository.findByUserId(userId = user.userId)
        assert(loginHistories.any { loginHistory -> loginHistory.isSuccess })
    }

    @Test
    @DisplayName("로그인 실패 이후 실패 이력을 저장한다.")
    fun saveUserHistoryAfterLoginFailTest() {
        // Given
        val token = UsernamePasswordAuthenticationToken(USERNAME, "${PASSWORD}!")

        // When
        try {
            authenticationManager.authenticate(token)
        } catch (e: AuthenticationException) {
            // BYPASS
        } finally {
            // Then
            val user = userRepository.findByUsername(USERNAME)
            val loginHistories = loginHistoryRepository.findByUserId(user?.userId)
            assert(loginHistories.any { loginHistory -> !loginHistory.isSuccess })
        }
    }

    private fun makeUserInvalid(user: User?) {
        (0 until 5).forEach { _ -> user?.handleLoginFailure() }
    }

}
