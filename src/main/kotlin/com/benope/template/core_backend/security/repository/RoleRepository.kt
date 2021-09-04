package com.benope.template.core_backend.security.repository

import com.benope.template.core_backend.security.domain.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String>