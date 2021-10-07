package com.benope.verbose.spoon.web.product.controller.dto

import com.benope.verbose.spoon.core_backend.common.validation.Patch
import com.benope.verbose.spoon.core_backend.common.validation.Post
import com.benope.verbose.spoon.core_backend.common.validation.Put
import com.benope.verbose.spoon.web.product.domain.brand.Brand
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class CategoryDto(
    @field:Null(groups = [Post::class, Put::class, Patch::class])
    var categoryId: Long?,
    @field:NotNull(groups = [Post::class])
    var brand: Brand?,
    @field:NotBlank(groups = [Post::class])
    val categoryName: String?,
) {
    constructor() : this(null, null, null)
}