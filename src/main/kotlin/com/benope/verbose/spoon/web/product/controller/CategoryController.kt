package com.benope.verbose.spoon.web.product.controller

import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.core_backend.common.exception.EntityNotFoundException
import com.benope.verbose.spoon.core_backend.common.validation.Patch
import com.benope.verbose.spoon.core_backend.common.validation.Post
import com.benope.verbose.spoon.web.product.controller.dto.CategoryDto
import com.benope.verbose.spoon.web.product.domain.category.Category
import com.benope.verbose.spoon.web.product.domain.category.CategoryRepository
import org.modelmapper.ModelMapper
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1/category")
@Validated
@Transactional
class CategoryController(
    private val categoryRepository: CategoryRepository,
    private val modelMapper: ModelMapper
) {

    @GetMapping
    fun get(): List<CategoryDto> {
        return categoryRepository.findAll()
            .map { modelMapper.map(it, CategoryDto::class.java) }
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable @NotNull id: Long?
    ): CategoryDto {
        return categoryRepository.findById(id!!)
            .map { modelMapper.map(it, CategoryDto::class.java) }
            .orElseThrow { throw EntityNotFoundException() }
    }

    @PostMapping
    fun post(
        @RequestBody @Validated(Post::class) categoryDto: CategoryDto,
        errors: BindingResult
    ): CategoryDto {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val entity = modelMapper.map(categoryDto, Category::class.java)
        val savedEntity = categoryRepository.save(entity)
        return modelMapper.map(savedEntity, CategoryDto::class.java)
    }

    @PatchMapping("/{id}")
    fun patch(
        @PathVariable @NotNull id: Long?,
        @RequestBody @Validated(Patch::class) categoryDto: CategoryDto,
        errors: BindingResult
    ): CategoryDto {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val entity = categoryRepository.findById(id!!).orElseThrow { throw EntityNotFoundException() }
        modelMapper.map(categoryDto, entity)
        val savedEntity = categoryRepository.save(entity)
        return modelMapper.map(savedEntity, CategoryDto::class.java)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable @NotNull id: Long?) {
        categoryRepository.deleteById(id!!)
    }


}