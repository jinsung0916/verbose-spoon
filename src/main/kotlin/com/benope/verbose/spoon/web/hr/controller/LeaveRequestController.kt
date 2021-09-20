package com.benope.verbose.spoon.web.hr.controller

import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.LeaveRequestResp
import com.benope.verbose.spoon.web.hr.service.LeaveRequestService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/leave-request")
class LeaveRequestController(
    private val leaveRequestService: LeaveRequestService
) {

    @PutMapping
    fun createLeaveRequest(
        @RequestBody @Valid createLeaveRequestReq: CreateLeaveRequestReq,
        errors: BindingResult,
        @AuthenticationPrincipal user: User
    ) : LeaveRequestResp {

        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        createLeaveRequestReq.userId = user.userId

        return leaveRequestService.createLeaveRequest(createLeaveRequestReq)
    }

}