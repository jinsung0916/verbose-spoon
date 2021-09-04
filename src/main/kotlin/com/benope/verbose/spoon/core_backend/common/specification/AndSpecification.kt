package com.benope.verbose.spoon.core_backend.common.specification

class AndSpecification<T>(
    private val specifications: List<Specification<T>>
) : Specification<T> {

    override fun isSatisfiedBy(t: T): Boolean {
        return specifications.all { it.isSatisfiedBy(t) }
    }

}