package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.domain.TimeOff

interface CustomizedTimeOffRepository {

    fun findByUserId(userId: Long?): TimeOff

    fun save(timeOff: TimeOff): TimeOff

}