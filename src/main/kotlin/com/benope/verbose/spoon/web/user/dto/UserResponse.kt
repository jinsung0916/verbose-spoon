package com.benope.verbose.spoon.web.user.dto

import com.benope.verbose.spoon.core_backend.security.domain.User

data class UserResponse(
    var username: String?,
    var firstName: String?,
    var lastName: String?,
    var nickname: String?,
    var email: String?,
) {
    companion object {
        fun fromUser(user: User?): UserResponse {
            return UserResponse(
                username = user?.username,
                firstName = user?.name?.firstName,
                lastName = user?.name?.lastName,
                nickname = user?.nickname?.value,
                email = user?.email?.value
            )
        }
    }
}