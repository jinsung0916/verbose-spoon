package com.benope.verbose.spoon.web.product.controller

import com.benope.verbose.spoon.core_backend.common.dto.PageableRequest
import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.core_backend.common.exception.EntityNotFoundException
import com.benope.verbose.spoon.core_backend.common.validation.Patch
import com.benope.verbose.spoon.core_backend.common.validation.Post
import com.benope.verbose.spoon.web.product.controller.dto.ProductDto
import com.benope.verbose.spoon.web.product.domain.product.Product
import com.benope.verbose.spoon.web.product.domain.product.ProductRepository
import org.modelmapper.ModelMapper
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1/product")
@Validated
class ProductController(
    private val productRepository: ProductRepository,
    private val modelMapper: ModelMapper
) {

    @GetMapping
    fun get(
        @RequestParam @NotNull categoryId: Long?,
        @ModelAttribute @Validated pageableRequest: PageableRequest,
        errors: BindingResult
    ): List<ProductDto> {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        return productRepository.findByCategoryId(categoryId, pageableRequest.toPageable())
            .map { modelMapper.map(it, ProductDto::class.java) }
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable @NotBlank id: String?
    ): ProductDto {
        return productRepository.findById(id!!)
            .map { modelMapper.map(it, ProductDto::class.java) }
            .orElseThrow { throw EntityNotFoundException() }
    }

    @PostMapping
    fun post(
        @RequestBody @Validated(Post::class) productDto: ProductDto,
        errors: BindingResult
    ): ProductDto {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val entity = modelMapper.map(productDto, Product::class.java)
        val savedEntity = productRepository.save(entity)
        return modelMapper.map(savedEntity, ProductDto::class.java)
    }

    @PatchMapping("/{id}")
    fun patch(
        @PathVariable @NotNull id: String?,
        @RequestBody @Validated(Patch::class) productDto: ProductDto,
        errors: BindingResult
    ): ProductDto {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val entity = productRepository.findById(id!!).orElseThrow { throw EntityNotFoundException() }
        modelMapper.map(productDto, entity)
        val savedEntity = productRepository.save(entity)
        return modelMapper.map(savedEntity, ProductDto::class.java)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable @NotNull id: String?) {
        productRepository.deleteById(id!!)
    }

}