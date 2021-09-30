package com.benope.verbose.spoon.core_backend.security.domain

import javax.persistence.Embeddable

@Embeddable
data class Nickname(
    var value: String?
) {
    override fun toString(): String {
        return this.value ?: ""
    }
}