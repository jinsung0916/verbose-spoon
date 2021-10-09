package com.benope.verbose.spoon.web.hr.infra

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffView
import com.benope.verbose.spoon.web.hr.repository.TimeOffViewRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class TimeOffViewRepositoryImpl(
    private val em: EntityManager
) : TimeOffViewRepository {

    override fun findAll(): List<TimeOffView> {
        val query =
            em.createQuery(
                "SELECT new com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffView(t, u) " +
                        "FROM TimeOffEntity t " +
                        "JOIN User u ON u.userId = t.userId " +
                        "ORDER BY u.userId, t.validityPeriod.startDate ",
                TimeOffView::class.java
            )

        return query.resultList
    }

    override fun findByUserId(userId: Long?): List<TimeOffView> {
        val query =
            em.createQuery(
                "SELECT new com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffView(t, u) " +
                        "FROM TimeOffEntity t " +
                        "JOIN User u ON u.userId = t.userId " +
                        "WHERE u.userId = :userId " +
                        "ORDER BY t.validityPeriod.startDate ",
                TimeOffView::class.java
            )

        query.setParameter("userId", userId)

        return query.resultList
    }

}