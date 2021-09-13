package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("SICK")
class SickLeave(
    userId: Long,
    period: LeavePeriod
) : LeaveRequestEntity(userId, period) {

    override fun totalTimeOffDay(): TimeOffDay {
        return TimeOffDay.ZERO
    }

    override fun getType(): LeaveRequestType {
        return LeaveRequestType.SICK
    }

}