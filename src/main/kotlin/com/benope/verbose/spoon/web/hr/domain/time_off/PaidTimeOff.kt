package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("PAID")
class PaidTimeOff(
    userId: Long,
    validityPeriod: ValidityPeriod,
    remainingDays: TimeOffDay
) : TimeOffEntity(
    userId,
    validityPeriod,
    remainingDays
) {
    override fun getType(): TimeOffType {
        return TimeOffType.PAID
    }

    override fun supports(leaveRequestType: LeaveRequestType?): Boolean {
        return leaveRequestType == LeaveRequestType.FULL_DAY ||
                leaveRequestType == LeaveRequestType.HALF_DAY
    }

}