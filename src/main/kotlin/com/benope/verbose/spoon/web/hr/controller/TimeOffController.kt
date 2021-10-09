package com.benope.verbose.spoon.web.hr.controller

import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffView
import com.benope.verbose.spoon.web.hr.dto.CreateTimeOffRequest
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

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createTimeOff(
        @RequestBody @Validated createTimeOffRequest: CreateTimeOffRequest,
        errors: BindingResult
    ): TimeOffView {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        return timeOffService.createTimeOff(createTimeOffRequest)
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.userId")
    fun findTimeOffByUserId(
        @RequestParam @NotNull userId: Long?
    ): List<TimeOffView> {
        return timeOffService.findTimeOffByUserId(userId)
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findAllTimeOff(): List<TimeOffView> {
        return timeOffService.findAllTimeOff()
    }

    @DeleteMapping("/{timeOffId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteTimeOff(@PathVariable @NotNull timeOffId: Long?) {
        timeOffService.deleteTimeOff(timeOffId)
    }


}