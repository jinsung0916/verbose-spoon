package com.benope.verbose.spoon.web.hr.service

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity

interface LeaveRequestNotifyService {

    fun notifyCreateToApprovalAuthority(leaveRequestEntity: LeaveRequestEntity)

    fun notifyApprovedToUser(leaveRequestEntity: LeaveRequestEntity)

}