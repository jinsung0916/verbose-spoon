package com.benope.verbose.spoon.core_backend.security.repository

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository
import com.benope.verbose.spoon.core_backend.security.domain.Role

interface RoleRepository : BaseRepository<Role, String>