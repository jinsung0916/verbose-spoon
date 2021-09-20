package com.benope.verbose.spoon.web.hr.exception

import com.benope.verbose.spoon.core_backend.common.exception.LocalizedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidLeaveRequestPeriodException : LocalizedException("exception.leave.request.invalid.period")