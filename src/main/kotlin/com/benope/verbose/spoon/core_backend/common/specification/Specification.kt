package com.benope.verbose.spoon.core_backend.common.specification

interface Specification<T> {

    fun isSatisfiedBy(t: T): Boolean

}