package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.core_backend.security.domain.Role
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.web.hr.exception.ApprovalLineNotAuthorizedException
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ApprovalLine private constructor(
    @field:Column(nullable = false)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApprovalLine

        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        return userId?.hashCode() ?: 0
    }

}