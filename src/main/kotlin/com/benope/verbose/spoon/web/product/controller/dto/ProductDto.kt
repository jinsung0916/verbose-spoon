package com.benope.verbose.spoon.web.product.controller.dto

import com.benope.verbose.spoon.core_backend.common.validation.Patch
import com.benope.verbose.spoon.core_backend.common.validation.Post
import com.benope.verbose.spoon.core_backend.common.validation.Put
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class ProductDto(
    @field:Null(groups = [Put::class, Patch::class])
    @field:NotBlank(groups = [Post::class])
    val productId: String?,
    @field:NotBlank(groups = [Post::class])
    val productName: String?,
    @field:NotNull(groups = [Post::class])
    val categoryId: Long?
) {
    constructor() : this(null, null, null)
}