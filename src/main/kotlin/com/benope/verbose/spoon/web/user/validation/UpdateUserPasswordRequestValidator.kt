package com.benope.verbose.spoon.web.user.validation

import com.benope.verbose.spoon.web.user.dto.UpdateUserPasswordRequest
import com.benope.verbose.spoon.web.user.exception.PasswordNotEqualException
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class UpdateUserPasswordRequestValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return UpdateUserPasswordRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        if (!hasValidPassword(target as UpdateUserPasswordRequest)) {
            errors.reject("exception.password.not.equal")
        }
    }

    fun validate(target: UpdateUserPasswordRequest?) {
        if (!hasValidPassword(target)) {
            throw PasswordNotEqualException()
        }
    }

    private fun hasValidPassword(updateUserPasswordRequest: UpdateUserPasswordRequest?): Boolean {
        val password = updateUserPasswordRequest?.password ?: ""
        val passwordConfirm = updateUserPasswordRequest?.passwordConfirm ?: ""
        return password == passwordConfirm
    }

}