package com.benope.verbose.spoon.web.hr.domain.time_off

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class TimeOffUsageHistory(
    @field:Column(nullable = false)
    var leaveApplicationId: Long?,
    var usedDays: TimeOffDay,
    var isCanceled: Boolean = false
) {

    fun undoHistory(): TimeOffDay {
        this.isCanceled = true
        return this.usedDays
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeOffUsageHistory

        if (leaveApplicationId != other.leaveApplicationId) return false

        return true
    }

    override fun hashCode(): Int {
        return leaveApplicationId?.hashCode() ?: 0
    }

}