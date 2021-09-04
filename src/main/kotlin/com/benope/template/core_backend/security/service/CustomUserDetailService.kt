package com.benope.template.core_backend.security.service

import com.benope.template.core_backend.security.exception.UserNotFoundException
import com.benope.template.core_backend.security.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        return userRepository.findByUsername(username) ?: throw UserNotFoundException()
    }

}