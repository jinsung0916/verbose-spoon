package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity

interface LeaveRequestRepository : BaseRepository<LeaveRequestEntity, Long>