package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.core_backend.security.domain.User
import java.time.LocalDate
import kotlin.streams.toList

class LeaveRequestView() {

    constructor(entity: LeaveRequestEntity?, user: User?) : this() {
        this.leaveRequestId = entity?.getId()
        this.type = entity?.getType()
        this.typeExplained = type?.title
        this.userId = entity?.getRequestUserId()
        this.startDate = entity?.getPeriod()?.startDate
        this.endDate = entity?.getPeriod()?.endDate
        this.days = entity?.getTotalTimeOffDay()?.days
        this.approvalLine = entity?.approvalLine?.stream()?.toList()
        this.isApproved = entity?.isApproved()

        this.username = user?.username
        this.nickname = user?.nickname?.value
    }

    var leaveRequestId: Long? = null
    var type: LeaveRequestType? = null
    var typeExplained: String? = null
    var userId: Long? = null
    var username: String? = null
    var nickname: String? = null
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null
    var days: Double? = null
    var approvalLine: List<ApprovalLine>? = null
    var isApproved: Boolean? = null

}