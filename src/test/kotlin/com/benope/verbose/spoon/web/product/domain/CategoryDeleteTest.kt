package com.benope.verbose.spoon.web.product.domain

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.web.product.domain.brand.Brand
import com.benope.verbose.spoon.web.product.domain.category.Category
import com.benope.verbose.spoon.web.product.domain.category.CategoryRepository
import com.benope.verbose.spoon.web.product.domain.product.Product
import com.benope.verbose.spoon.web.product.domain.product.ProductRepository
import com.benope.verbose.spoon.web.product.exception.CategoryCannotBeDeletedException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class CategoryDeleteTest(
    @Autowired private val categoryRepository: CategoryRepository,
    @Autowired private val productRepository: ProductRepository
) : BenopeTest() {

    @Test
    @DisplayName("Category 에 1 개 이상의 Product 가 등록된 경우 삭제가 불가능하다.")
    fun categorySafeDeleteTest() {
        val category = Category(
            brand = Brand.BENOPE,
            categoryName = "20bar"
        )

        val savedEntity = categoryRepository.saveAndFlush(category)

        val product = Product(
            productId = "productId",
            productName = "productName",
            categoryId = savedEntity.categoryId!!
        )

        productRepository.saveAndFlush(product)

        assertThrows<CategoryCannotBeDeletedException> {
            categoryRepository.deleteById(savedEntity.categoryId!!)
        }
    }

}