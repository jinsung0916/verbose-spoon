package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffView

interface TimeOffViewRepository {

    fun findAll(): List<TimeOffView>

    fun findByUserId(userId: Long?): List<TimeOffView>

}