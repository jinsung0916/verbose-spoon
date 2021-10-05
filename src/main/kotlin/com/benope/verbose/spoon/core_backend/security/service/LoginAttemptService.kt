package com.benope.verbose.spoon.core_backend.security.service

import com.benope.verbose.spoon.core_backend.security.domain.LoginHistory
import com.benope.verbose.spoon.core_backend.security.repository.LoginHistoryRepository
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Service
class LoginAttemptService(
    private val userRepository: UserRepository,
    private val loginHistoryRepository: LoginHistoryRepository,
    private val request: HttpServletRequest
) {

    @Transactional
    fun handleLoginSuccess(username: String?) {
        val user = userRepository.findByUsername(username)
        user?.let {
            user.handleLoginSuccess()
            userRepository.save(user)

            val loginHistory = LoginHistory(
                userId = user.userId,
                loginIp = request.remoteAddr,
                loginUserAgent = request.getHeader("User-Agent")
            )
            loginHistoryRepository.save(loginHistory)
        }
    }

    @Transactional
    fun handleLoginFailure(username: String?) {
        val user = userRepository.findByUsername(username)
        user?.let {
            user.handleLoginFailure()
            userRepository.save(user)

            val loginHistory = LoginHistory(
                userId = user.userId,
                isSuccess = false,
                loginIp = request.remoteAddr,
                loginUserAgent = request.getHeader("User-Agent")
            )
            loginHistoryRepository.save(loginHistory)
        }
    }

}