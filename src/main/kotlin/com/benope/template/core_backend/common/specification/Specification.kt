package com.benope.template.core_backend.common.specification

interface Specification<T> {

    fun isSatisfiedBy(t: T): Boolean

}