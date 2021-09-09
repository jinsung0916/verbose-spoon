package com.benope.verbose.spoon.web.user.controller

import com.benope.verbose.spoon.core_backend.common.dto.PageableRequest
import com.benope.verbose.spoon.core_backend.common.exception.DtoValidationException
import com.benope.verbose.spoon.web.user.dto.CreateUserRequest
import com.benope.verbose.spoon.web.user.dto.UpdateUserPasswordRequest
import com.benope.verbose.spoon.web.user.dto.UpdateUserRequest
import com.benope.verbose.spoon.web.user.dto.UserResponse
import com.benope.verbose.spoon.web.user.service.UserManageService
import com.benope.verbose.spoon.web.user.validation.UpdateUserPasswordRequestValidator
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserManageController(
    private val userManageService: UserManageService,
    private val updateUserPasswordRequestValidator: UpdateUserPasswordRequestValidator
) {

    @PutMapping("/")
    fun createUser(
        @RequestBody @Valid createUserRequest: CreateUserRequest,
        errors: BindingResult
    ): UserResponse {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val user = userManageService.createUser(createUserRequest)
        return UserResponse.fromUser(user)
    }

    @GetMapping("/list")
    fun findUserList(
        @ModelAttribute @Valid pageableRequest: PageableRequest,
        errors: BindingResult
    ): Page<UserResponse> {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        return userManageService.findUserList(pageableRequest.toPageable())
            .map { UserResponse.fromUser(it) }
    }

    @GetMapping("/{username}")
    fun findUser(
        @PathVariable username: String
    ): UserResponse {
        return UserResponse.fromUser(userManageService.findUser(username))
    }

    @PostMapping("/{username}")
    fun updateUser(
        @PathVariable username: String,
        @RequestBody @Valid updateUserRequest: UpdateUserRequest,
        errors: BindingResult
    ): UserResponse {
        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val user = userManageService.updateUser(username = username, updateUserRequest = updateUserRequest)
        return UserResponse.fromUser(user)
    }

    @DeleteMapping("/{username}")
    fun deleteUser(
        @PathVariable username: String
    ) {
        userManageService.deleteUser(username)
    }

    @PostMapping("/{username}/password")
    fun updateUserPassword(
        @PathVariable username: String,
        @RequestBody @Valid updateUserPasswordRequest: UpdateUserPasswordRequest,
        errors: BindingResult
    ): UserResponse {
        updateUserPasswordRequestValidator.validate(updateUserPasswordRequest, errors)

        if (errors.hasErrors()) {
            throw DtoValidationException(errors.fieldErrors)
        }

        val user = userManageService.updateUserPassword(username, updateUserPasswordRequest)
        return UserResponse.fromUser(user)
    }

    @DeleteMapping("/{username}/password")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun resetUserPassword(
        @PathVariable username: String
    ) {
        userManageService.resetUserPassword(username)
    }

}