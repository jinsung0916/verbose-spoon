package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.exception.InvalidTimeOffDayException
import com.benope.verbose.spoon.web.hr.exception.InvalidValidityPeriodException
import com.benope.verbose.spoon.web.hr.repository.TimeOffRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class TimeOffCreateTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val timeOffRepositoryImpl: TimeOffRepository
) : BenopeTest() {

    @Test
    @DisplayName("연차를 등록한다.")
    fun createTimeOffTest() {
        val user = userRepository.findByUsername(USERNAME)
        val timeOffEntity = PaidTimeOff(
            userId = user?.userId!!,
            validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now()),
            remainingDays = TimeOffDay(10.0)
        )

        val savedEntity = timeOffRepositoryImpl.save(timeOffEntity)

        assertThat(savedEntity.timeOffId).isNotNull
    }

    @Test
    @DisplayName("연차를 등록한다 - 시작일이 종료일보다 빠르면 예외를 발생시킨다.")
    fun createTimeOffExceptionTest1() {
        val user = userRepository.findByUsername(USERNAME)

        val startDate = LocalDate.now()
        val endDate = startDate.minusDays(1)

        assertThrows<InvalidValidityPeriodException> {
            PaidTimeOff(
                userId = user?.userId!!,
                validityPeriod = ValidityPeriod(startDate, endDate),
                remainingDays = TimeOffDay(10.5)
            )
        }
    }

    @Test
    @DisplayName("연차를 등록한다 - 잔여 연차가 0 미만일 경우 예외를 발생시킨다.")
    fun createTimeOffExceptionTest2() {
        val user = userRepository.findByUsername(USERNAME)

        val remainingDays = -1.0

        assertThrows<IllegalArgumentException> {
            PaidTimeOff(
                userId = user?.userId!!,
                validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now()),
                remainingDays = TimeOffDay(remainingDays)
            )
        }
    }

    @Test
    @DisplayName("연차를 등록한다 - 잔여 연차가 0.5 단위가 아닐 경우 예외를 발생시킨다.")
    fun createTimeOffExceptionTest3() {
        val user = userRepository.findByUsername(USERNAME)

        val remainingDays = 1.3

        assertThrows<InvalidTimeOffDayException> {
            PaidTimeOff(
                userId = user?.userId!!,
                validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now()),
                remainingDays = TimeOffDay(remainingDays)
            )
        }
    }


}