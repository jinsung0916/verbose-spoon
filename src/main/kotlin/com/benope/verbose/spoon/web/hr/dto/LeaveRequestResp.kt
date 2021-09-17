package com.benope.verbose.spoon.web.hr.dto

import com.benope.verbose.spoon.web.hr.domain.leave_request.ApprovalLine
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import java.time.LocalDate

data class LeaveRequestResp private constructor(
    var leaveRequestId: Long?,
    var type: LeaveRequestType?,
    var userId: Long?,
    var startDate: LocalDate?,
    var endDate: LocalDate?,
    var approvalLine: List<ApprovalLine>?,
    var isApproved: Boolean?
) {
    constructor() : this(null, null, null, null, null, null, null)
}