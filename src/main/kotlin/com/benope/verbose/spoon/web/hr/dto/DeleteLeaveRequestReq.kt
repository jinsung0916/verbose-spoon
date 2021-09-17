package com.benope.verbose.spoon.web.hr.dto

import javax.validation.constraints.NotNull

data class DeleteLeaveRequestReq(

    @field:NotNull
    var leaveRequestId: Long?,

    var requestUserId: Long?

)