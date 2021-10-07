package com.benope.verbose.spoon.core_backend.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFoundException : LocalizedException("exception.entity.not.found")