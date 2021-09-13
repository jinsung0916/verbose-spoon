package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.web.hr.dto.CreateTimeOffRequest

enum class TimeOffType {
    PAID {
        override fun toEntity(createTimeOffRequest: CreateTimeOffRequest?): TimeOffEntity {
            return PaidTimeOff(
                userId = createTimeOffRequest?.userId!!,
                validityPeriod = ValidityPeriod(createTimeOffRequest.startDate!!, createTimeOffRequest.endDate!!),
                remainingDays = TimeOffDay(createTimeOffRequest.remainingDays!!)
            )
        }
    },
    DELEGATE {
        override fun toEntity(createTimeOffRequest: CreateTimeOffRequest?): TimeOffEntity {
            TODO("Not yet implemented")
        }
    };

    abstract fun toEntity(createTimeOffRequest: CreateTimeOffRequest?) : TimeOffEntity
}