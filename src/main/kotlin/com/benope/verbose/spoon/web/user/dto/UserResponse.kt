package com.benope.verbose.spoon.web.user.dto

import com.benope.verbose.spoon.core_backend.security.domain.User
import java.time.LocalDate

data class UserResponse(
    var username: String?,
    var firstName: String?,
    var lastName: String?,
    var nickname: String?,
    var email: String?,
    var createDate: LocalDate?,
    var updateDate: LocalDate?
) {
    companion object {
        fun fromUser(user: User?): UserResponse {
            return UserResponse(
                username = user?.username,
                firstName = user?.name?.firstName,
                lastName = user?.name?.lastName,
                nickname = user?.nickname?.value,
                email = user?.email?.value,
                createDate = user?.createDateTime?.toLocalDate(),
                updateDate = user?.lastModifiedDatetime?.toLocalDate()
            )
        }
    }
}