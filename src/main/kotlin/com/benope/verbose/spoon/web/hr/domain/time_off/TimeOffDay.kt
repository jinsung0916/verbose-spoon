package com.benope.verbose.spoon.web.hr.domain.time_off;

import javax.persistence.Embeddable

@Embeddable
data class TimeOffDay(
    var days: Double
) {

    constructor(days: Int) : this(days.toDouble())

    init {
        if (days < 0) throw  IllegalArgumentException("TimeOffDay value cannot be less than zero. (value: $days)")
        if ((days.rem(0.5)) != 0.0) throw  IllegalArgumentException("TimeOffDay value is invalid: $days")
    }

    companion object {
        val ZERO = TimeOffDay(0.0)
        val HALF = TimeOffDay(0.5)
    }

    fun plus(o: TimeOffDay): TimeOffDay {
        return TimeOffDay(this.days + o.days())
    }

    fun minus(o: TimeOffDay): TimeOffDay {
        val minusValue = this.days - o.days()
        return if (minusValue > 0) {
            TimeOffDay(minusValue)
        } else {
            ZERO
        }
    }

    fun days(): Double {
        return this.days;
    }

}
