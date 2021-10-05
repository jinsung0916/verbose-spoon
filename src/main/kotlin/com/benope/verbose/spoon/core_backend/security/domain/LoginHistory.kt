package com.benope.verbose.spoon.core_backend.security.domain

import com.benope.verbose.spoon.core_backend.common.audit.AuditEntity
import javax.persistence.*

@Entity
@Table
class LoginHistory(
    @Column(nullable = false)
    private val userId: Long?,
    val isSuccess: Boolean = true,
    val loginIp: String?,
    val loginUserAgent: String?
) : AuditEntity<LoginHistory>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}