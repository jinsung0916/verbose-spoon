package com.benope.verbose.spoon.core_backend.common.specification

import com.benope.verbose.spoon.BenopeTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SpecificationTest : BenopeTest() {

    data class MockClass(
        val value: Int
    )

    companion object {
        val spec1 = object : Specification<MockClass> {
            override fun isSatisfiedBy(t: MockClass): Boolean {
                return t.value > 1
            }
        }
        val spec2 = object : Specification<MockClass> {
            override fun isSatisfiedBy(t: MockClass): Boolean {
                return t.value > 5
            }
        }
        val spec3 = object : Specification<MockClass> {
            override fun isSatisfiedBy(t: MockClass): Boolean {
                return t.value > 10
            }
        }
    }

    @Test
    @DisplayName("AndSpecification 이 정상적으로 동작 한다.")
    fun andSpecificationTest() {
        // Given
        val mockClass = MockClass(10)

        // When
        val andSpec1 = AndSpecification(listOf(spec1, spec2))
        val andSpec2 = AndSpecification(listOf(spec1, spec3))

        // Then
        assert(andSpec1.isSatisfiedBy(mockClass))
        assert(!andSpec2.isSatisfiedBy(mockClass))
    }

    @Test
    @DisplayName("OrSpecification 이 정상적으로 동작 한다.")
    fun orSpecificationTest() {
        // Given
        val mockClass = MockClass(10)

        // When
        val orSpec1 = OrSpecification(listOf(spec1, spec3))
        val orSpec2 = OrSpecification(listOf(spec3, spec3))

        // Then
        assert(orSpec1.isSatisfiedBy(mockClass))
        assert(!orSpec2.isSatisfiedBy(mockClass))
    }

}