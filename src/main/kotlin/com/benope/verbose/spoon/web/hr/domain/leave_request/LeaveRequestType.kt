package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.user.service.UserManageService

enum class LeaveRequestType(title: String) {

    FULL_DAY("연차") {
        override fun doGetEntity(leaveRequestDto: CreateLeaveRequestReq): LeaveRequestEntity {
            return FullDayLeave(
                userId = leaveRequestDto.userId!!,
                period = LeavePeriod(
                    startDate = leaveRequestDto.startDate!!,
                    endDate = leaveRequestDto.endDate!!
                )
            )
        }
    },
    HALF_DAY("반차") {
        override fun doGetEntity(leaveRequestDto: CreateLeaveRequestReq): LeaveRequestEntity {
            return HalfDayLeave(
                userId = leaveRequestDto.userId!!,
                period = LeavePeriod(
                    startDate = leaveRequestDto.startDate!!,
                    endDate = leaveRequestDto.endDate!!
                )
            )
        }
    },
    SICK("병가") {
        override fun doGetEntity(leaveRequestDto: CreateLeaveRequestReq): LeaveRequestEntity {
            return SickLeave(
                userId = leaveRequestDto.userId!!,
                period = LeavePeriod(
                    startDate = leaveRequestDto.startDate!!,
                    endDate = leaveRequestDto.endDate!!
                )
            )
        }
    };

    fun getEntity(leaveRequestDto: CreateLeaveRequestReq, userManageService: UserManageService): LeaveRequestEntity {
        val entity = doGetEntity(leaveRequestDto)

        leaveRequestDto.approvalLine?.stream()
            ?.map { ApprovalLine.fromUser(userManageService.findUserById(it)) }
            ?.forEach { entity.addApprovalLine(it) }
        return entity
    }

    abstract fun doGetEntity(leaveRequestDto: CreateLeaveRequestReq): LeaveRequestEntity

    var title: String

    init {
        this.title = title
    }
}