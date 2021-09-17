package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.user.service.UserManageService

enum class LeaveRequestType {
    FULL_DAY {
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
    HALF_DAY {
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
    SICK {
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
}