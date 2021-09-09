package com.benope.verbose.spoon.web.user.dto

import com.benope.verbose.spoon.core_backend.security.domain.FullName
import com.benope.verbose.spoon.core_backend.security.domain.Nickname
import com.benope.verbose.spoon.core_backend.security.domain.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank
    var firstName: String?,
    @field:NotBlank
    var lastName: String?,
    @field:NotBlank
    var nickname: String?,
    @field:Email
    var email: String?
) {
    fun updateUserEntity(user: User?) {
        user?.name = FullName(firstName = this.firstName, lastName = this.lastName)
        user?.nickname = Nickname(this.nickname)
        user?.email = com.benope.verbose.spoon.core_backend.security.domain.Email(this.email)
    }
}