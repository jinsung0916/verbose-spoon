package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.domain.LeaveRequest
import com.benope.verbose.spoon.web.hr.exception.NotEnoughTimeOffException
import com.benope.verbose.spoon.web.hr.repository.TimeOffRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import kotlin.random.Random

class TimeOffUsageTest(
    @Autowired private val timeOffRepository: TimeOffRepository,
    @Autowired private val userRepository: UserRepository
) : BenopeTest() {

    @BeforeEach
    fun setUp() {
        val user = userRepository.findByUsername(USERNAME)
        timeOffRepository.save(
            NoOpTimeOff(
                userId = user?.userId!!,
                validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now().plusMonths(1)),
                remainingDays = TimeOffDay(1.0)
            )
        )
        timeOffRepository.save(
            NoOpTimeOff(
                userId = user?.userId!!,
                validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now().plusMonths(1)),
                remainingDays = TimeOffDay(2.0)
            )
        )
    }

    @Test
    @DisplayName("연차를 소진한다.")
    fun timeOffUsageTest() {
        // Given
        val leaveRequest = mock<LeaveRequest>() {
            on { getId() } doReturn Random.nextLong()
            on { totalTimeOffDay() } doReturn TimeOffDay(1.0)
        }

        val user = userRepository.findByUsername(USERNAME)
        val timeOff = timeOffRepository.findByUserId(user?.userId)

        // When
        val processedTimeOffDays = timeOff.useTimeOff(leaveRequest, leaveRequest.totalTimeOffDay())
        val savedTimeOff = timeOffRepository.save(timeOff)

        // Then
        assertThat(processedTimeOffDays).isEqualTo(leaveRequest.totalTimeOffDay())
        assertThat(savedTimeOff.usedDays()).isEqualTo(leaveRequest.totalTimeOffDay())
    }

    @Test
    @DisplayName("연차를 소진한다 - 잔여 연차가 부족할 경우 예외를 발생시킨다.")
    fun timeOffUsageExceptionTest() {
        // Given
        val leaveRequest = mock<LeaveRequest>() {
            on { getId() } doReturn Random.nextLong()
            on { totalTimeOffDay() } doReturn TimeOffDay(5.0)
        }

        // Then
        assertThrows<NotEnoughTimeOffException> {
            // When
            val user = userRepository.findByUsername(USERNAME)
            val timeOff = timeOffRepository.findByUserId(user?.userId)
            timeOff.useTimeOff(leaveRequest, leaveRequest.totalTimeOffDay())
        }
    }

    @Test
    @DisplayName("연차 사용을 취소한다.")
    fun undoTimeOffUsageTest() {
        // Given
        val user = userRepository.findByUsername(USERNAME)
        val timeOff = timeOffRepository.findByUserId(user?.userId)

        val leaveRequest = mock<LeaveRequest>() {
            on { getId() } doReturn Random.nextLong()
            on { totalTimeOffDay() } doReturn TimeOffDay(1.0)
        }
        timeOff.useTimeOff(leaveRequest, leaveRequest.totalTimeOffDay())
        val savedTimeOff = timeOffRepository.save(timeOff)
        val remainingDays = savedTimeOff.remainingDays()

        // When
        savedTimeOff.undoUseTimeOff(leaveRequest.getId())
        val resavedTimeOff = timeOffRepository.save(savedTimeOff)

        // Then
        assertThat(resavedTimeOff.remainingDays())
            .isEqualTo(remainingDays.plus(leaveRequest.totalTimeOffDay()))
    }

    @Entity
    @DiscriminatorValue("NO_OP")
    class NoOpTimeOff(
        userId: Long,
        validityPeriod: ValidityPeriod,
        remainingDays: TimeOffDay
    ) : TimeOffEntity(
        userId,
        validityPeriod,
        remainingDays
    ) {
        override fun getType(): TimeOffType {
            TODO("Not yet implemented")
        }

        override fun supports(leaveRequest: LeaveRequest): Boolean {
            return true
        }
    }

}