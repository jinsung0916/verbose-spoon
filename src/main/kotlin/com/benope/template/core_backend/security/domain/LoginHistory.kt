package com.benope.template.core_backend.security.domain

import com.benope.template.core_backend.common.domain.AuditEntity
import javax.persistence.*

@Entity
@Table
class LoginHistory(
    @Column(nullable = false)
    private val userId: Long?,
    val isSuccess: Boolean = true
) : AuditEntity<LoginHistory>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}