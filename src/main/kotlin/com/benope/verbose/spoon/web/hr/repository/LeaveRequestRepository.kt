package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface LeaveRequestRepository : JpaRepository<LeaveRequestEntity, Long> {

    @Query(
        "SELECT l " +
        "FROM LeaveRequestEntity l " +
        "WHERE l.userId = :userId " +
        "AND l.isDeleted = false "
    )
    fun findByUserId(userId: Long?): List<LeaveRequestEntity>

    @Query(
        "SELECT l " +
        "FROM LeaveRequestEntity l " +
        "WHERE l.period.startDate >= :startDate " +
        "AND l.period.startDate <= :endDate " +
        "AND l.isDeleted = false"
    )
    fun findAllByPeriod(startDate: LocalDate?, endDate: LocalDate?): List<LeaveRequestEntity>

    @Query(
        "SELECT l " +
        "FROM LeaveRequestEntity l " +
        "JOIN l.approvalLine a " +
        "WHERE a.userId = :userId " +
        "AND l.isDeleted = false "
    )
    fun findByApprovalLineUserId(userId: Long?): List<LeaveRequestEntity>

}