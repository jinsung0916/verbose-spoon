package com.benope.verbose.spoon.core_backend.common.jpa

import com.benope.verbose.spoon.core_backend.security.domain.User
import com.fasterxml.jackson.annotation.JsonIgnore
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
open class BaseEntity<A : AbstractAggregateRoot<A>?> : AbstractAggregateRoot<A>() {

    companion object {
        const val NOT_DELETED_CLAUSE = "is_deleted = false"
    }

    @JsonIgnore
    @CreatedDate
    open var createDateTime: LocalDateTime? = null
        protected set

    @JsonIgnore
    @CreatedBy
    open var createdBy: Long? = null
        protected set

    @JsonIgnore
    @LastModifiedDate
    open var lastModifiedDatetime: LocalDateTime? = null
        protected set

    @JsonIgnore
    @LastModifiedBy
    open var lastModifiedBy: Long? = null
        protected set

    // Version property for optimistic locking.
    @JsonIgnore
    @Version
    open var version: Int? = null
        protected set

    // Delete flag for soft delete
    @JsonIgnore
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