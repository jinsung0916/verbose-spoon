package com.benope.verbose.spoon.web.hr.controller

import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.web.hr.dto.CreateTimeOffRequest
import com.benope.verbose.spoon.web.hr.dto.TimeOffResponse
import com.benope.verbose.spoon.web.hr.service.TimeOffService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1/time-off")
@Validated
class TimeOffController(
    private val timeOffService: TimeOffService
) {

    @PutMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createTimeOff(
        @RequestBody @Validated createTimeOffRequest: CreateTimeOffRequest,
        errors: BindingResult
    ): TimeOffResponse {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        return timeOffService.createTimeOff(createTimeOffRequest)
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.userId")
    fun findTimeOffByUserId(
        @RequestParam @NotNull userId: Long?
    ): List<TimeOffResponse> {
        return timeOffService.findTimeOffByUserId(userId)
    }

    @DeleteMapping("/{timeOffId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteTimeOff(@PathVariable @NotNull timeOffId: Long?) {
        timeOffService.deleteTimeOff(timeOffId)
    }


}