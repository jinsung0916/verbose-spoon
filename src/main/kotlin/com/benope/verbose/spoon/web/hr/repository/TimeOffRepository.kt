package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity

interface TimeOffRepository : BaseRepository<TimeOffEntity, Long>, CustomizedTimeOffRepository