package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.web.hr.exception.InvalidLeaveRequestPeriodException
import java.time.LocalDate
import javax.persistence.Embeddable

@Embeddable
data class LeavePeriod(
    var startDate: LocalDate,
    var endDate: LocalDate
) {

    init {
        initCheck(startDate, endDate)
    }

    private fun initCheck(startDate: LocalDate, endDate: LocalDate) {
        if (startDate.isAfter(endDate)) {
            throw InvalidLeaveRequestPeriodException()
        }
    }

    fun daysBetween(): Int {
        return startDate.until(endDate).days + 1
    }

}