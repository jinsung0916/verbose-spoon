package com.benope.verbose.spoon.web.hr.domain.leave_request.event

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import com.benope.verbose.spoon.web.hr.service.LeaveRequestNotifyService
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class LeaveRequestCreatedEventHandler(
    private val leaveRequestNotifyService: LeaveRequestNotifyService
) : ApplicationListener<LeaveRequestCreatedEvent> {

    @Async
    override fun onApplicationEvent(event: LeaveRequestCreatedEvent) {
        leaveRequestNotifyService.notifyCreateToApprovalAuthority(event.source as LeaveRequestEntity)
    }

}