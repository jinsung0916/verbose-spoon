package com.benope.verbose.spoon.web.product.domain.product

import com.benope.verbose.spoon.core_backend.common.jpa.BaseRepository
import org.springframework.data.domain.Pageable

interface ProductRepository : BaseRepository<Product, String> {

    fun findByCategoryId(categoryId: Long?, pageable: Pageable): List<Product>

}