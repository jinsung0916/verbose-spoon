package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestView
import java.time.LocalDate

interface LeaveRequestViewRepository {

    fun findById(leaveRequestId: Long?): LeaveRequestView

    fun findByUserId(userId: Long?): List<LeaveRequestView>

    fun findAllByPeriod(startDate: LocalDate?, endDate: LocalDate?): List<LeaveRequestView>

    fun findByApprovalLineUserId(userId: Long?): List<LeaveRequestView>

}