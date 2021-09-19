package com.benope.verbose.spoon.web.user.dto

import com.benope.verbose.spoon.core_backend.security.domain.User

data class UserResponse(
    var userId: Long?,
    var username: String?,
    var firstName: String?,
    var lastName: String?,
    var nickname: String?,
    var email: String?,
    var authority: Set<String>?
) {
    companion object {
        fun fromUser(user: User?): UserResponse {
            return UserResponse(
                userId = user?.userId,
                username = user?.username,
                firstName = user?.name?.firstName,
                lastName = user?.name?.lastName,
                nickname = user?.nickname?.value,
                email = user?.email?.value,
                authority = user?.authorities?.map { it.authority }?.toSet()
            )
        }
    }
}