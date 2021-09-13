package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.domain.LeaveRequest
import com.benope.verbose.spoon.web.hr.exception.TimeOffUnableToDeleteException
import com.benope.verbose.spoon.web.hr.repository.TimeOffRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import kotlin.random.Random

class TimeOffDeleteTest(
    @Autowired private val timeOffRepository: TimeOffRepository,
    @Autowired private val userRepository: UserRepository
) : BenopeTest() {

    @BeforeEach
    fun setUp() {
        val user = userRepository.findByUsername(USERNAME)
        timeOffRepository.save(
            TimeOffUsageTest.NoOpTimeOff(
                userId = user?.userId!!,
                validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now().plusMonths(1)),
                remainingDays = TimeOffDay(1.0)
            )
        )
    }

    @Test
    @DisplayName("처리된 휴가가 존재할 경우 연차를 삭제할 수 없다.")
    fun timeOffDeleteExceptionTest() {

        val leaveRequest = mock<LeaveRequest>() {
            on { getId() } doReturn Random.nextLong()
            on { totalTimeOffDay() } doReturn TimeOffDay.HALF
        }

        val user = userRepository.findByUsername(USERNAME)
        val timeOff = timeOffRepository.findByUserId(user?.userId)
        timeOff.useTimeOff(leaveRequest, leaveRequest.totalTimeOffDay())

        timeOffRepository.save(timeOff)

        val timeOffEntity = timeOffRepository.findAll().stream()
            .findAny()
            .get()

        assertThrows<TimeOffUnableToDeleteException> {
            timeOffRepository.deleteById(timeOffEntity.timeOffId)
        }
    }

}