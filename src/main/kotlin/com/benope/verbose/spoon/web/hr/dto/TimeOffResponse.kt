package com.benope.verbose.spoon.web.hr.dto

import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffType
import com.benope.verbose.spoon.web.user.dto.UserResponse
import java.time.LocalDate

class TimeOffResponse constructor() {

    constructor(timeOffEntity: TimeOffEntity?, user: User?) : this() {
        this.timeOffId = timeOffEntity?.timeOffId
        this.type = timeOffEntity?.getType()
        this.startDate = timeOffEntity?.validityPeriod?.startDate
        this.endDate = timeOffEntity?.validityPeriod?.endDate
        this.remainingDays = timeOffEntity?.remainingDays()?.days
        this.usedDays = timeOffEntity?.usedDays()?.days
        this.reason = timeOffEntity?.reason

        this.userId = timeOffEntity?.userId
        this.user = UserResponse.fromUser(user)
    }

    var timeOffId: Long? = null
    var type: TimeOffType? = null
    var userId: Long? = null
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null
    var remainingDays: Double? = null
    var usedDays: Double? = null
    var reason: String? = null

    var user: UserResponse? = null

}