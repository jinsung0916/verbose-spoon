package com.benope.verbose.spoon.web.hr.dto

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffType
import java.time.LocalDate
import javax.validation.constraints.NotNull

data class CreateTimeOffRequest(

    @field:NotNull
    var type: TimeOffType?,

    @field:NotNull
    var userId: Long?,

    @field:NotNull
    var startDate: LocalDate?,

    @field:NotNull
    var endDate: LocalDate?,

    @field:NotNull
    var remainingDays: Double?

) {
    fun toEntity(): TimeOffEntity {
        return type!!.toEntity(this)
    }
}