package com.benope.verbose.spoon.web.hr.controller

import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.DeleteLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp
import com.benope.verbose.spoon.web.hr.service.LeaveRequestService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1/leave-request")
@Validated
class LeaveRequestController(
    private val leaveRequestService: LeaveRequestService
) {

    @PutMapping
    fun createLeaveRequest(
        @RequestBody @Validated createLeaveRequestReq: CreateLeaveRequestReq,
        errors: BindingResult,
        @AuthenticationPrincipal user: User
    ): LeaveRequestResp {

        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        createLeaveRequestReq.userId = user.userId

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
        @PathVariable @NotNull   leaveRequestId: Long?,
        @AuthenticationPrincipal user: User?
    ) {
        val deleteLeaveRequestReq = DeleteLeaveRequestReq(
            leaveRequestId = leaveRequestId,
            requestUserId = user?.userId
        )
        leaveRequestService.deleteLeaveRequest(deleteLeaveRequestReq)
    }

}