package com.benope.template.core_backend.security.domain

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class JwtToken(
    var value: String,
) : Serializable
