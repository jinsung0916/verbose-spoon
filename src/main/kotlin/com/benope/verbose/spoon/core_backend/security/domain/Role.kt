package com.benope.verbose.spoon.core_backend.security.domain

import com.benope.verbose.spoon.core_backend.common.audit.AuditEntity
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
class Role(
    @Id
    val roleId: String
) : AuditEntity<Role>() {

    companion object {
        val ROLE_ADMIN = Role("ROLE_ADMIN")
        val ROLE_USER = Role("ROLE_USER")
        val ROLE_APPROVAL = Role("ROLE_APPROVAL")
    }

}