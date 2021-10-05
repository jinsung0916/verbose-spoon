package com.benope.verbose.spoon.web.product.domain.product

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository

interface ProductRepository : BaseRepository<Product, String> {

    fun findByCategoryId(categoryId: Long?) : List<Product>

}