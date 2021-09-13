package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.core_backend.security.domain.Role
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.web.hr.exception.ApprovalLineNotAuthorizedException
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
data class ApprovalLine private constructor(
    var userId: Long?,
    var isApproved: Boolean = false,
    var approveDateTime: LocalDateTime? = null
) {
    companion object {
        fun fromUser(user: User?): ApprovalLine {
            user ?: throw IllegalArgumentException("User cannot be null")

            if (user.hasRole(Role.ROLE_APPROVAL) || user.hasRole(Role.ROLE_ADMIN)) {
                return ApprovalLine(
                    userId = user.userId
                )
            } else {
                throw ApprovalLineNotAuthorizedException()
            }
        }
    }

    fun approveRequest(userId: Long?) {
        if (this.userId == userId) {
            this.isApproved = true
            this.approveDateTime = LocalDateTime.now()
        } else {
            throw ApprovalLineNotAuthorizedException()
        }
    }
}