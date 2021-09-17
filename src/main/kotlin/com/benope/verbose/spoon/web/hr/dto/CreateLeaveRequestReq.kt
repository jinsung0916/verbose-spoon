package com.benope.verbose.spoon.web.hr.dto

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.user.service.UserManageService
import java.time.LocalDate
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateLeaveRequestReq(

    @field:NotNull
    var type: LeaveRequestType?,

    @field:NotNull
    var startDate: LocalDate?,

    @field:NotNull
    var endDate: LocalDate?,

    var userId: Long?,

    @field:NotEmpty
    var approvalLine: List<Long>?

) {
    fun toEntity(userManageService: UserManageService): LeaveRequestEntity {
        return type!!.getEntity(this, userManageService)
    }
}

