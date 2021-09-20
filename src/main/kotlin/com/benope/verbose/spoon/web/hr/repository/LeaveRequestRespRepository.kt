package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp
import java.time.LocalDate

interface LeaveRequestRespRepository {

    fun findById(leaveRequestId: Long?): LeaveRequestResp

    fun findByUserId(userId: Long?): List<LeaveRequestResp>

    fun findAllByPeriod(startDate: LocalDate?, endDate: LocalDate?): List<LeaveRequestResp>

    fun findByApprovalLineUserId(userId: Long?): List<LeaveRequestResp>

}