package com.benope.verbose.spoon.core_backend.common.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.LocalDateTime
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
open class AuditEntity<A : AbstractAggregateRoot<A>?> : AbstractAggregateRoot<A>() {

    @CreatedDate
    private var createDateTime: LocalDateTime? = null

    private var createdBy: Long? = null

    @CreatedDate
    private var lastModifiedDatetime: LocalDateTime? = null

    private var lastModifiedBy: Long? = null

    // Version property for optimistic locking.
    @Version
    private var version: Int? = null

}