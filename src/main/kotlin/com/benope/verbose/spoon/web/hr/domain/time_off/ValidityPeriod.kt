package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.web.hr.exception.InvalidValidityPeriodException
import java.time.LocalDate
import javax.persistence.Embeddable

@Embeddable
data class ValidityPeriod(
    var startDate: LocalDate,
    var endDate: LocalDate
) {

    init {
        initCheck(startDate, endDate)
    }

    private fun initCheck(startDate: LocalDate, endDate: LocalDate) {
        if (startDate.isAfter(endDate)) {
            throw InvalidValidityPeriodException()
        }
    }

    fun valid(): Boolean {
        val today = LocalDate.now()
        return includeDay(today)
    }

    private fun includeDay(day: LocalDate?): Boolean {
        day ?: throw IllegalArgumentException("A day cannot be null.")

        return day.isEqual(startDate)
                || (day.isAfter(startDate) && day.isBefore(endDate))
                || day.isEqual(endDate)
    }

    fun daysBetween(): Int {
        return endDate.until(startDate).days
    }

}