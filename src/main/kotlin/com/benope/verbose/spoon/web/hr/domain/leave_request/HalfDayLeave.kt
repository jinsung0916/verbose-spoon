package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import com.benope.verbose.spoon.web.hr.exception.InvalidHalfDayPeriodException
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
            throw InvalidHalfDayPeriodException()
        }
    }

    override fun getTotalTimeOffDay(): TimeOffDay {
        return TimeOffDay.HALF
    }

    override fun getType(): LeaveRequestType {
        return LeaveRequestType.HALF_DAY
    }

}