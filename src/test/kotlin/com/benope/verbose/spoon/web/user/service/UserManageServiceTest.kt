package com.benope.verbose.spoon.web.user.service

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.web.user.dto.CreateUserRequest
import com.benope.verbose.spoon.web.user.dto.UpdateUserRequest
import com.benope.verbose.spoon.web.user.exception.DuplicatedUserException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser


class UserManageServiceTest(
    @Autowired private val userManageService: UserManageService,
    @Autowired private val passwordEncoder: PasswordEncoder
) : BenopeTest() {

    @Test
    @DisplayName("새로운 사용자를 등록한다.")
    @WithMockUser(roles = ["ADMIN"])
    fun registerUserTest() {
        // Given
        val createUserRequest = CreateUserRequest(
            username = "benope",
            password = "benope",
            firstName = "Ahn",
            lastName = "JinSung",
            nickname = "AJ",
            email = "AJ@benope.com"
        )

        // When
        val user = userManageService.createUser(createUserRequest)

        // Then
        assertThat(user?.username).isEqualTo(createUserRequest.username)
        assertThat(passwordEncoder.matches(createUserRequest.password, user?.password)).isTrue
        assertThat(user?.name?.firstName).isEqualTo(createUserRequest.firstName)
        assertThat(user?.name?.lastName).isEqualTo(createUserRequest.lastName)
        assertThat(user?.nickname?.value).isEqualTo(createUserRequest.nickname)
        assertThat(user?.email?.value).isEqualTo(createUserRequest.email)
    }

    @Test
    @DisplayName("중복된 사용자 이름을 등록할 경우 예외 처리한다.")
    @WithMockUser(roles = ["ADMIN"])
    fun blockDuplicatedUserTest() {
        // Given
        val createUserRequest = CreateUserRequest(
            username = USERNAME,
            password = PASSWORD,
            firstName = "Ahn",
            lastName = "JinSung",
            nickname = "AJ",
            email = "AJ@benope.com"
        )

        // Then
        assertThrows<DuplicatedUserException> {
            // When
            userManageService.createUser(createUserRequest)
        }
    }

    @Test
    @DisplayName("사용자 정보를 갱신한다. - 관리자")
    @WithMockUser(roles = ["ADMIN"])
    fun updateUserByAdminTest() {
        doUpdateUser()
    }

    @Test
    @DisplayName("사용자 정보를 갱신한다. - 사용자")
    @WithMockUser(username = USERNAME)
    fun updateUserByUserTest() {
        doUpdateUser()
    }

    private fun doUpdateUser() {
        // Given
        val updateUserRequest = UpdateUserRequest(
            firstName = "updatedFirstNAme",
            lastName = "updatedLastNAme",
            nickname = "updatedNickname",
            email = "updatedEmail"
        )

        // When
        val savedUser = userManageService.updateUser(USERNAME, updateUserRequest)

        // Then
        assertThat(savedUser.name?.firstName).isEqualTo(updateUserRequest.firstName)
        assertThat(savedUser.name?.lastName).isEqualTo(updateUserRequest.lastName)
        assertThat(savedUser.nickname?.value).isEqualTo(updateUserRequest.nickname)
        assertThat(savedUser.email?.value).isEqualTo(updateUserRequest.email)
    }

    @Test
    @DisplayName("사용자 리스트를 조회한다.")
    fun findUserListTest() {
        // Given
        val pageable: Pageable = Pageable.unpaged()

        // When
        val users = userManageService.findUserList(pageable)

        // Then
        assertThat(users?.isEmpty).isFalse
    }

    @Test
    @DisplayName("사용자 세부 정보를 조회한다. - 일반 사용자")
    @WithMockUser(username = USERNAME, roles = ["USER"])
    fun findUserByUserTest() {
        doFindUser(USERNAME)
    }


    @Test
    @DisplayName("사용자 세부 정보를 조회한다. - 관리자")
    @WithMockUser(roles = ["ADMIN"])
    fun findUserByAdminTest() {
        doFindUser(USERNAME)
    }

    private fun doFindUser(username: String?) {
        // Given

        // When
        val user = userManageService.findUser(username)

        // Then
        assertThat(user.username).isEqualTo(username)
    }

}