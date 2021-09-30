package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.dto.TimeOffResponse

interface TimeOffResponseRepository {

    fun findAll(): List<TimeOffResponse>

    fun findByUserId(userId: Long?): List<TimeOffResponse>

}