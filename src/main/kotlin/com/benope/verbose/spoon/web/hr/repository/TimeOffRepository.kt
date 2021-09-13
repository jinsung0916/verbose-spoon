package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TimeOffRepository : JpaRepository<TimeOffEntity, Long?>, CustomizedTimeOffRepository