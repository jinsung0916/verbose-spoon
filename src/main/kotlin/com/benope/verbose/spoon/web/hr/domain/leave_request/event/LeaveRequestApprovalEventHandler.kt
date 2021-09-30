package com.benope.verbose.spoon.web.hr.domain.leave_request.event

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import com.benope.verbose.spoon.web.hr.service.LeaveRequestNotifyService
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class LeaveRequestApprovalEventHandler(
    private val leaveRequestNotifyService: LeaveRequestNotifyService
) : ApplicationListener<LeaveRequestApprovalEvent> {

    @Async
    override fun onApplicationEvent(event: LeaveRequestApprovalEvent) {
        leaveRequestNotifyService.notifyApprovedToUser(event.source as LeaveRequestEntity)
    }

}