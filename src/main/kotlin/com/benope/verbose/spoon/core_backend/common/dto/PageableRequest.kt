package com.benope.verbose.spoon.core_backend.common.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import javax.validation.constraints.NotNull

data class PageableRequest(
    @field: NotNull val page: Int?,
    @field: NotNull val size: Int?
) {
    fun toPageable(): Pageable {
        return PageRequest.of(page!!, size!!)
    }
}