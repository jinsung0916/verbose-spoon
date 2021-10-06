package com.benope.verbose.spoon.web.hr.infra

import com.benope.verbose.spoon.web.hr.dto.TimeOffResponse
import com.benope.verbose.spoon.web.hr.repository.TimeOffResponseRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class TimeOffResponseRepositoryImpl(
    private val em: EntityManager
) : TimeOffResponseRepository {

    override fun findAll(): List<TimeOffResponse> {
        val query =
            em.createQuery(
                "SELECT new com.benope.verbose.spoon.web.hr.dto.TimeOffResponse(t, u) " +
                        "FROM TimeOffEntity t " +
                        "JOIN User u ON u.userId = t.userId " +
                        "ORDER BY u.userId, t.validityPeriod.startDate ",
                TimeOffResponse::class.java
            )

        return query.resultList
    }

    override fun findByUserId(userId: Long?): List<TimeOffResponse> {
        val query =
            em.createQuery(
                "SELECT new com.benope.verbose.spoon.web.hr.dto.TimeOffResponse(t, u) " +
                        "FROM TimeOffEntity t " +
                        "JOIN User u ON u.userId = t.userId " +
                        "WHERE u.userId = :userId " +
                        "ORDER BY t.validityPeriod.startDate ",
                TimeOffResponse::class.java
            )

        query.setParameter("userId", userId)

        return query.resultList
    }

}