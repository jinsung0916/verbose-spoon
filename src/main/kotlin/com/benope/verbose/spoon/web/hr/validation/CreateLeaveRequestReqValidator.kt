package com.benope.verbose.spoon.web.hr.validation

import com.benope.verbose.spoon.core_backend.common.exception.LocalizedException
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus

@Component
class CreateLeaveRequestReqValidator  {

    companion object {
        private const val EXCEPTION_LEAVE_REQUEST_NOT_VALID_PERIOD = "exception.leave.request.not.valid.period"
        private const val EXCEPTION_HALF_DAY_NOT_VALID_PERIOD = "exception.half.day.not.valid.period"
    }

    fun validate(target: CreateLeaveRequestReq?) {

        if (!isValidLeaveRequestPeriod(target)) {
            throw NotValidLeaveRequestPeriodException()
        }

        if (!isValidHalfDay(target)) {
            throw NotValidHalfDayPeriodException()
        }
    }

    private fun isValidLeaveRequestPeriod(target: CreateLeaveRequestReq?): Boolean {
        return target?.endDate?.isAfter(target?.startDate) ?: false
                || target?.endDate?.isEqual(target?.startDate) ?: false
    }

    private fun isValidHalfDay(target: CreateLeaveRequestReq?): Boolean {
        return target?.type != LeaveRequestType.HALF_DAY
                || (target?.startDate?.isEqual(target?.endDate) ?: false)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class NotValidLeaveRequestPeriodException : LocalizedException(EXCEPTION_LEAVE_REQUEST_NOT_VALID_PERIOD)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class NotValidHalfDayPeriodException : LocalizedException(EXCEPTION_HALF_DAY_NOT_VALID_PERIOD)

}