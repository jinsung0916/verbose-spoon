package com.benope.verbose.spoon.web.hr.service

import com.benope.verbose.spoon.core_backend.common.exception.EntityNotFoundException
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.DeleteLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp
import com.benope.verbose.spoon.web.hr.repository.LeaveRequestRepository
import com.benope.verbose.spoon.web.hr.repository.LeaveRequestRespRepository
import com.benope.verbose.spoon.web.user.service.UserManageService
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.transaction.Transactional

@Service
@Transactional
class LeaveRequestService(
    private val leaveRequestRepository: LeaveRequestRepository,
    private val leaveRequestRespRepository: LeaveRequestRespRepository,
    private val timeOffService: TimeOffService,
    private val userManageService: UserManageService
) {

    fun createLeaveRequest(request: CreateLeaveRequestReq?): LeaveRequestResp {
        request ?: throw IllegalArgumentException("CreateLeaveRequestReq cannot be null.")

        val leaveRequestEntity = request.toEntity(userManageService)
        val savedEntity = leaveRequestRepository.save(leaveRequestEntity)

        timeOffService.useTimeOff(
            request.userId,
            leaveRequestEntity.getId(),
            leaveRequestEntity.getType(),
            leaveRequestEntity.getTotalTimeOffDay()
        )

        return LeaveRequestResp(savedEntity, null)
    }

    fun findByUserId(userId: Long?): List<LeaveRequestResp> {
        return leaveRequestRespRepository.findByUserId(userId)
    }

    fun findByApprovalLineUserId(approveUserId: Long?): List<LeaveRequestResp> {
        return leaveRequestRespRepository.findByApprovalLineUserId(approveUserId)
    }

    private fun findById(leaveRequestId: Long?): LeaveRequestEntity {
        leaveRequestId ?: throw IllegalArgumentException("LeaveRequestId cannot be null.")

        return leaveRequestRepository.findById(leaveRequestId).orElseThrow { EntityNotFoundException() }
    }

    fun findAllApproved(startDate: LocalDate?, endDate: LocalDate?): List<LeaveRequestResp> {
        startDate ?: throw IllegalArgumentException("StartDate cannot be null.")
        endDate ?: throw IllegalArgumentException("EndDate cannot be null.")

        return leaveRequestRespRepository.findAllByPeriod(startDate, endDate)
            .filter { it.isApproved ?: false }
    }

    fun deleteLeaveRequest(request: DeleteLeaveRequestReq?) {
        request ?: throw IllegalArgumentException("DeleteLeaveRequestReq cannot be null.")

        val leaveRequest = findById(request.leaveRequestId)
        val requestUser = userManageService.findUserById(request.requestUserId)

        leaveRequest.markDeleted(requestUser)
        val savedEntity = leaveRequestRepository.save(leaveRequest)

        timeOffService.undoUseTimeOff(savedEntity.getId(), savedEntity.getRequestUserId())
    }

    fun approveRequest(leaveRequestId: Long?, approveUserId: Long?) {
        leaveRequestId ?: throw IllegalArgumentException("LeaveRequestId cannot be null.")
        approveUserId ?: throw IllegalArgumentException("ApproveUserId cannot be null.")

        val leaveRequest = findById(leaveRequestId)
        leaveRequest.approveRequest(approveUserId)
        leaveRequestRepository.save(leaveRequest)
    }

}