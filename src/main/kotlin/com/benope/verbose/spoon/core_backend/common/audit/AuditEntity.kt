package com.benope.verbose.spoon.core_backend.common.audit

import com.benope.verbose.spoon.core_backend.security.domain.User
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.Authentication
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class AuditEntity<A : AbstractAggregateRoot<A>?> : AbstractAggregateRoot<A>() {

    @CreatedDate
    private var createDateTime: LocalDateTime? = null

    @CreatedBy
    private var createdBy: Long? = null

    @LastModifiedDate
    private var lastModifiedDatetime: LocalDateTime? = null

    @LastModifiedBy
    private var lastModifiedBy: Long? = null

    // Version property for optimistic locking.
    @Version
    private var version: Int? = null

    // Delete flag for soft delete
    private var isDeleted: Boolean = false

    open fun markDeleted() {
        isDeleted = true
    }

    fun setCreatedBy(authentication: Authentication?) {
        val principal = authentication?.principal
        if (principal is User) {
            this.createdBy = principal.userId
        }
    }

    fun setLastModifiedBy(authentication: Authentication?) {
        val principal = authentication?.principal
        if (principal is User) {
            this.lastModifiedBy = principal.userId
        }
    }

}