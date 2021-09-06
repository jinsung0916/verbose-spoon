package com.benope.verbose.spoon.core_backend.security.domain

import javax.persistence.Embeddable

@Embeddable
data class Email(
    var value: String?
)