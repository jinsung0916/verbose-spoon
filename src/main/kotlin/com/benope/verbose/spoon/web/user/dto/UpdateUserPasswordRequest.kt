package com.benope.verbose.spoon.web.user.dto

import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotBlank

data class UpdateUserPasswordRequest(
    @field:NotBlank
    val password: String?,
    @field:NotBlank
    val passwordConfirm: String?
) {
    fun updateUserEntity(user: User?, passwordEncoder: PasswordEncoder?) {
        user?.setPassword(this.password, passwordEncoder)
    }
}
