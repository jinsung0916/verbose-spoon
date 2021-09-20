package com.benope.verbose.spoon.web.hr.infra

import com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp
import com.benope.verbose.spoon.web.hr.repository.LeaveRequestRespRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.EntityManager

@Repository
class LeaveRequestRespRepositoryImpl(
    private val em: EntityManager
) : LeaveRequestRespRepository {

    override fun findById(leaveRequestId: Long?): LeaveRequestResp {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE l.userId = :leaveRequestId " +
                    "AND l.isDeleted = false " +
                    "AND u.isDeleted = false ",
            LeaveRequestResp::class.java
        )
            .setParameter("leaveRequestId", leaveRequestId)
            .singleResult
    }

    override fun findByUserId(userId: Long?): List<LeaveRequestResp> {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE l.userId = :userId " +
                    "AND l.isDeleted = false " +
                    "AND u.isDeleted = false ",
            LeaveRequestResp::class.java
        )
            .setParameter("userId", userId)
            .resultList
    }

    override fun findAllByPeriod(startDate: LocalDate?, endDate: LocalDate?): List<LeaveRequestResp> {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE l.period.startDate >= :startDate " +
                    "AND l.period.startDate <= :endDate " +
                    "AND l.isDeleted = false " +
                    "AND u.isDeleted = false ",
            LeaveRequestResp::class.java
        )
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .resultList
    }

    override fun findByApprovalLineUserId(userId: Long?): List<LeaveRequestResp> {
        return em.createQuery(
            "SELECT new com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp(l, u) " +
                    "FROM LeaveRequestEntity l " +
                    "JOIN l.approvalLine a " +
                    "LEFT JOIN User u ON u.userId = l.userId " +
                    "WHERE a.userId = :userId " +
                    "AND l.isDeleted = false " +
                    "AND u.isDeleted = false ",
            LeaveRequestResp::class.java
        )
            .setParameter("userId", userId)
            .resultList
    }
}