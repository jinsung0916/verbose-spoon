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
    open var createDateTime: LocalDateTime? = null
        protected set

    @CreatedBy
    open var createdBy: Long? = null
        protected set

    @LastModifiedDate
    open var lastModifiedDatetime: LocalDateTime? = null
        protected set

    @LastModifiedBy
    open var lastModifiedBy: Long? = null
        protected set

    // Version property for optimistic locking.
    @Version
    open var version: Int? = null
        protected set

    // Delete flag for soft delete
    open var isDeleted: Boolean = false
        protected set

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