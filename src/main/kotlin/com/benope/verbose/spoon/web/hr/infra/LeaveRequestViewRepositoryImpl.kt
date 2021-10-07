package com.benope.verbose.spoon.web.hr.infra

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestView
import com.benope.verbose.spoon.web.hr.repository.LeaveRequestViewRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.EntityManager

@Repository
class LeaveRequestViewRepositoryImpl(
    private val em: EntityManager
) : LeaveRequestViewRepository {

    override fun findById(leaveRequestId: Long?): LeaveRequestView {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestView(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE l.userId = :leaveRequestId ",
            LeaveRequestView::class.java
        )
            .setParameter("leaveRequestId", leaveRequestId)
            .singleResult
    }

    override fun findByUserId(userId: Long?): List<LeaveRequestView> {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestView(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE l.userId = :userId ",
            LeaveRequestView::class.java
        )
            .setParameter("userId", userId)
            .resultList
    }

    override fun findAllByPeriod(startDate: LocalDate?, endDate: LocalDate?): List<LeaveRequestView> {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestView(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE l.period.startDate BETWEEN :startDate AND :endDate " +
                    "AND l.period.endDate BETWEEN :startDate AND :endDate ",
            LeaveRequestView::class.java
        )
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .resultList
    }

    override fun findByApprovalLineUserId(userId: Long?): List<LeaveRequestView> {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestView(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "JOIN l.approvalLine a " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE a.userId = :userId " +
                    "AND a.isApproved = false ",
            LeaveRequestView::class.java
        )
            .setParameter("userId", userId)
            .resultList
    }
}