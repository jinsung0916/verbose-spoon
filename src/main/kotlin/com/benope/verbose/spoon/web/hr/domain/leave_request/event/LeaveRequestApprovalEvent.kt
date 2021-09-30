package com.benope.verbose.spoon.web.hr.domain.leave_request.event

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import org.springframework.context.ApplicationEvent

class LeaveRequestApprovalEvent(
    leaveRequestEntity: LeaveRequestEntity
) : ApplicationEvent(leaveRequestEntity)