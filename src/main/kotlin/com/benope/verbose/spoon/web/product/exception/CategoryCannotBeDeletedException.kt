package com.benope.verbose.spoon.web.product.exception

import com.benope.verbose.spoon.core_backend.common.exception.LocalizedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CategoryCannotBeDeletedException : LocalizedException("exception.category.cannot.be.deleted")