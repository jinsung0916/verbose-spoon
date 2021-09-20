package com.benope.verbose.spoon.web.hr.repository

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LeaveRequestRepository : JpaRepository<LeaveRequestEntity, Long>