package com.benope.verbose.spoon.web.hr.service

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffType
import com.benope.verbose.spoon.web.hr.dto.CreateTimeOffRequest
import com.benope.verbose.spoon.web.hr.repository.TimeOffRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class TimeOffServiceTest(
    @Autowired private val timeOffService: TimeOffService
) : BenopeTest() {

    var user: User? = null
    var admin: User? = null

    @BeforeEach
    fun setup(
        @Autowired userRepository: UserRepository,
        @Autowired timeOffRepository: TimeOffRepository
    ) {
        user = userRepository.findByUsername(USERNAME)
        admin = userRepository.findByUsername(ADMIN_USERNAME)
    }

    @Test
    @DisplayName("연차를 생성한다.")
    fun createTimeOffTest() {
        val createTimeOffRequest = CreateTimeOffRequest(
            type = TimeOffType.PAID,
            userId = user?.userId,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusYears(1),
            remainingDays = 10.0
        )

        val response = timeOffService.createTimeOff(createTimeOffRequest)

        assertThat(response.type).isEqualTo(createTimeOffRequest.type)
        assertThat(response.userId).isEqualTo(createTimeOffRequest.userId)
        assertThat(response.remainingDays).isEqualTo(createTimeOffRequest.remainingDays)
    }

}