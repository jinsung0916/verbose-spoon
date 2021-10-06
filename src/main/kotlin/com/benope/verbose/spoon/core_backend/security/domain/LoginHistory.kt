package com.benope.verbose.spoon.core_backend.security.domain

import com.benope.verbose.spoon.core_backend.common.jpa.BaseEntity
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table
@Where(clause = BaseEntity.NOT_DELETED_CLAUSE)
class LoginHistory(
    @Column(nullable = false)
    private val userId: Long?,
    val isSuccess: Boolean = true,
    val loginIp: String?,
    val loginUserAgent: String?,
    val message: String? = null
) : BaseEntity<LoginHistory>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}