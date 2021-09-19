package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("HALF_DAY")
class HalfDayLeave(
    userId: Long,
    period: LeavePeriod
) : LeaveRequestEntity(userId, period) {

    init {
        if (period.startDate != period.endDate) {
            throw IllegalArgumentException("StartDate and endDate should be equal.")
        }
    }

    override fun getTotalTimeOffDay(): TimeOffDay {
        return TimeOffDay.HALF
    }

    override fun getType(): LeaveRequestType {
        return LeaveRequestType.HALF_DAY
    }

}