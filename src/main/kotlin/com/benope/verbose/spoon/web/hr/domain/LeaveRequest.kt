package com.benope.verbose.spoon.web.hr.domain

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay

interface LeaveRequest {

    fun getId(): Long?

    fun totalTimeOffDay(): TimeOffDay

    fun getType() : LeaveRequestType

    fun getRequestUserId() : Long

}