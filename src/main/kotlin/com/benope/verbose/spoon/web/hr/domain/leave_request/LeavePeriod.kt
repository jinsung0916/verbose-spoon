package com.benope.verbose.spoon.web.hr.domain.leave_request

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
            throw IllegalArgumentException("StartDate cannot be before endDate.")
        }
    }

    fun daysBetween(): Int {
        return startDate.until(endDate).days + 1
    }

}