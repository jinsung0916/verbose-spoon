package com.benope.verbose.spoon.web.user.controller

import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.web.user.dto.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/session")
class SessionController {

    @GetMapping
    fun getCurrentSessionUser(
        @AuthenticationPrincipal user: User
    ): UserResponse {
        return UserResponse.fromUser(user)
    }

}