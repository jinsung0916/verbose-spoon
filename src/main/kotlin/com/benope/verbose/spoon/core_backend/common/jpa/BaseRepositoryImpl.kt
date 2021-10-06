package com.benope.verbose.spoon.core_backend.common.jpa

import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import javax.persistence.EntityManager

class BaseRepositoryImpl<T : BaseEntity<T>, ID : Serializable>(
    entityInformation: JpaEntityInformation<T, ID>,
    private val em: EntityManager
) : SimpleJpaRepository<T, ID>(entityInformation, em), BaseRepository<T, ID> {

    companion object {
        private const val HARD_DELETE_NOT_SUPPORTED = "Hard delete is not supported."
    }

    override fun delete(entity: T) {
        safeDelete(entity)
    }

    override fun deleteById(id: ID) {
        val entity = findById(id).orElseThrow { throw NullPointerException("Entity not found.") }
        safeDelete(entity)
    }

    override fun deleteAllById(ids: MutableIterable<ID>) {
        throw UnsupportedOperationException(HARD_DELETE_NOT_SUPPORTED)
    }

    override fun deleteAll() {
        throw UnsupportedOperationException(HARD_DELETE_NOT_SUPPORTED)
    }

    override fun deleteAll(entities: MutableIterable<T>) {
        throw UnsupportedOperationException(HARD_DELETE_NOT_SUPPORTED)
    }

    private fun safeDelete(entity: T) {
        entity.markDeleted()
        save(entity)
    }

    override fun refresh(entity: T) {
        em.refresh(entity)
    }

}