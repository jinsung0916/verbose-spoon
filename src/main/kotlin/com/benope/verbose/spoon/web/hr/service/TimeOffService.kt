package com.benope.verbose.spoon.web.hr.service

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffView
import com.benope.verbose.spoon.web.hr.dto.CreateTimeOffRequest
import com.benope.verbose.spoon.web.hr.repository.TimeOffRepository
import com.benope.verbose.spoon.web.hr.repository.TimeOffViewRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class TimeOffService(
    private val timeOffRepository: TimeOffRepository,
    private val timeOffViewRepository: TimeOffViewRepository
) {

    fun useTimeOff(
        requestUserId: Long?,
        leaveRequestId: Long?,
        leaveRequestType: LeaveRequestType?,
        requiredTimeOff: TimeOffDay?
    ) {
        requestUserId ?: throw IllegalArgumentException("RequestUserId cannot be null.")
        leaveRequestId ?: throw IllegalArgumentException("LeaveRequestId cannot be null.")
        leaveRequestType ?: throw IllegalArgumentException("LeaveRequestType cannot be null.")
        requiredTimeOff ?: throw IllegalArgumentException("RequiredTimeOff cannot be null.")

        val timeOff = timeOffRepository.findByUserId(requestUserId)
        timeOff.useTimeOff(leaveRequestId, leaveRequestType, requiredTimeOff)
        timeOffRepository.save(timeOff)
    }

    fun undoUseTimeOff(leaveRequestId: Long?, requestUserId: Long?) {
        leaveRequestId ?: throw IllegalArgumentException("LeaveRequestId cannot be null.")
        requestUserId ?: throw IllegalArgumentException("RequestUserId cannot be null.")

        val timeOff = timeOffRepository.findByUserId(requestUserId)
        timeOff.undoUseTimeOff(leaveRequestId)
        timeOffRepository.save(timeOff)
    }

    fun createTimeOff(createTimeOffRequest: CreateTimeOffRequest?): TimeOffView {
        createTimeOffRequest ?: throw IllegalArgumentException("CreateTimeOffRequest cannot be null.")

        val entity = createTimeOffRequest.toEntity()
        val savedEntity = timeOffRepository.save(entity)
        return TimeOffView(savedEntity, null)
    }

    fun findTimeOffByUserId(userId: Long?): List<TimeOffView> {
        userId ?: throw IllegalArgumentException("UserId cannot be null.")

        return timeOffViewRepository.findByUserId(userId)
    }

    fun deleteTimeOff(timeOffId: Long?) {
        timeOffId ?: throw IllegalArgumentException("TimeOffId cannot be null.")

        timeOffRepository.deleteById(timeOffId)
    }

    fun findAllTimeOff(): List<TimeOffView> {
        return timeOffViewRepository.findAll()
    }

}