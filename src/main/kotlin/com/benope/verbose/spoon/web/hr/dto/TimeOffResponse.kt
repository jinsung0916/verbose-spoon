package com.benope.verbose.spoon.web.hr.dto

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffType
import java.time.LocalDate

data class TimeOffResponse private constructor(
    var timeOffId: Long?,
    var type: TimeOffType?,
    var userId: Long?,
    var startDate: LocalDate?,
    var endDate: LocalDate?,
    var remainingDays: Double?,
    var usedDays: Double?,
    var reason: String?
) {
    constructor() : this(null, null, null, null, null, null, null, null)
}