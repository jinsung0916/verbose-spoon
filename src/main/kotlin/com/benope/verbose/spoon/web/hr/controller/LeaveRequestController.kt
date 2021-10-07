package com.benope.verbose.spoon.web.hr.controller

import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.core_backend.security.util.getUserId
import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.DeleteLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp
import com.benope.verbose.spoon.web.hr.service.LeaveRequestService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1/leave-request")
@Validated
class LeaveRequestController(
    private val leaveRequestService: LeaveRequestService
) {

    @PostMapping
    fun createLeaveRequest(
        @RequestBody @Validated createLeaveRequestReq: CreateLeaveRequestReq,
        errors: BindingResult
    ): LeaveRequestResp {

        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        createLeaveRequestReq.userId = getUserId()

        return leaveRequestService.createLeaveRequest(createLeaveRequestReq)
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.userId")
    fun findByUserId(
        @RequestParam @NotNull userId: Long?
    ): List<LeaveRequestResp> {
        return leaveRequestService.findByUserId(userId)
    }

    @DeleteMapping("/{leaveRequestId}")
    fun deleteLeaveRequest(
        @PathVariable @NotNull leaveRequestId: Long?,
    ) {
        val deleteLeaveRequestReq = DeleteLeaveRequestReq(
            leaveRequestId = leaveRequestId,
            requestUserId = getUserId()
        )
        leaveRequestService.deleteLeaveRequest(deleteLeaveRequestReq)
    }

    @GetMapping("/approval/list")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #approvalUserId == authentication.principal.userId")
    fun findLeaveRequestByApprovalUserId(
        @RequestParam @NotNull approvalUserId: Long?
    ): List<LeaveRequestResp> {
        return leaveRequestService.findByApprovalLineUserId(approvalUserId)
    }

    @PutMapping("/approval/{leaveRequestId}")
    @PreAuthorize("hasRole('ROLE_APPROVAL')")
    fun approveLeaveRequest(
        @PathVariable @NotNull leaveRequestId: Long?
    ) {
        leaveRequestService.approveRequest(leaveRequestId = leaveRequestId, approveUserId = getUserId())
    }

    @GetMapping("/list/approved")
    fun findAllApproved(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull startDate: LocalDate?,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull endDate: LocalDate?
    ): List<LeaveRequestResp> {
        return leaveRequestService.findAllApproved(startDate, endDate)
    }

}