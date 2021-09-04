package com.benope.template.core_backend.security.domain

import com.benope.template.core_backend.common.domain.AuditEntity
import javax.persistence.*

@Entity
@Table
class Role(
    @Id
    val roleId: String
) : AuditEntity<Role>()