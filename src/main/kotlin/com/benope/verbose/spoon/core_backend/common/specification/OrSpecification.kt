package com.benope.verbose.spoon.core_backend.common.specification

class OrSpecification<T>(
    private val specifications: List<Specification<T>>
) : Specification<T> {

    override fun isSatisfiedBy(t: T): Boolean {
        return specifications.any { it.isSatisfiedBy(t) }
    }

}