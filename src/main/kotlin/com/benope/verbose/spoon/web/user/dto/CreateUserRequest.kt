package com.benope.verbose.spoon.web.user.dto

import com.benope.verbose.spoon.core_backend.security.domain.*
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateUserRequest(
    @field: NotBlank
    val username: String?,
    @field: NotBlank
    val password: String?,
    @field: NotBlank
    val firstName: String?,
    @field: NotBlank
    val lastName: String?,
    @field: NotBlank
    val nickname: String?,
    @field: NotNull
    @field: javax.validation.constraints.Email
    val email: String?
) {
    fun toUserEntity(passwordEncoder: PasswordEncoder): User {
        val user = User(
            username = this.username!!,
            password = passwordEncoder.encode(this.password),
            name = FullName(firstName = this.firstName, lastName = this.lastName),
            nickname = Nickname(this.nickname),
            email = Email(this.email)
        )
        user.addAuthority(Role.ROLE_USER)
        return user
    }
}