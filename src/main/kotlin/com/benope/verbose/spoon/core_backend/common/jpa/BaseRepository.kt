package com.benope.verbose.spoon.core_backend.common.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

@NoRepositoryBean
interface BaseRepository<T : BaseEntity<T>, ID : Serializable> : JpaRepository<T, ID>