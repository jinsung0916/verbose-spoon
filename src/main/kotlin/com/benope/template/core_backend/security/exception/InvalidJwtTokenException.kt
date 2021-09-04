package com.benope.template.core_backend.security.exception

import com.benope.template.core_backend.common.exception.LocalizedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidJwtTokenException : LocalizedException("exception.jwt.token.invalid")