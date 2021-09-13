package com.benope.verbose.spoon.web.hr.infra

import com.benope.verbose.spoon.web.hr.domain.TimeOff
import com.benope.verbose.spoon.web.hr.domain.time_off.DelegatingTimeOff
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity
import com.benope.verbose.spoon.web.hr.repository.CustomizedTimeOffRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.transaction.Transactional
import kotlin.streams.toList

@Repository
@Transactional
class TimeOffRepositoryImpl(
    private val em: EntityManager,
    private val timeOffComparator: Comparator<TimeOffEntity>
) : CustomizedTimeOffRepository {

    override fun findByUserId(userId: Long?): TimeOff {
        val query =
            em.createQuery(
                "SELECT t FROM TimeOffEntity t WHERE t.userId = :userId AND t.isDeleted = false",
                TimeOffEntity::class.java
            )

        query.setParameter("userId", userId)
        return DelegatingTimeOff(query.resultList, timeOffComparator)
    }

    override fun deleteById(timeOffId: Long?) {
        val timeOffEntity = em.find(TimeOffEntity::class.java, timeOffId)
            ?: throw IllegalArgumentException("TimeOffEntity not found for id: $timeOffId")
        timeOffEntity.markDeleted()
        merge(timeOffEntity)
    }

    override fun save(timeOff: TimeOff): TimeOff {
        val entityList = timeOff.toEntityList().stream()
            .map {
                if (em.contains(it)) {
                    persist(it)
                } else {
                    merge(it)
                }
            }.toList()
        return DelegatingTimeOff(entityList, timeOffComparator)
    }

    private fun persist(timeOffEntity: TimeOffEntity): TimeOffEntity {
        val authentication = SecurityContextHolder.getContext().authentication
        timeOffEntity.setCreatedBy(authentication)
        em.persist(timeOffEntity)
        return timeOffEntity
    }

    private fun merge(timeOffEntity: TimeOffEntity): TimeOffEntity {
        val authentication = SecurityContextHolder.getContext().authentication
        timeOffEntity.setLastModifiedBy(authentication)
        return em.merge(timeOffEntity)
    }

}