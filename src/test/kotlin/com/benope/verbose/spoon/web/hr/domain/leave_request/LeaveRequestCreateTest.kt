package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import com.benope.verbose.spoon.web.hr.repository.LeaveRequestRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class LeaveRequestCreateTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val leaveRequestRepository: LeaveRequestRepository
) : BenopeTest() {

    @Test
    @DisplayName("휴가 신청을 생성한다(반차).")
    fun createHalfDayLeaveTest() {
        val user = userRepository.findByUsername(USERNAME)

        val leaveRequest = HalfDayLeave(
            userId = user?.userId!!,
            period = LeavePeriod(
                startDate = LocalDate.now(),
                endDate = LocalDate.now()
            )
        )

        val savedRequest = leaveRequestRepository.save(leaveRequest)

        assertThat(savedRequest.getId()).isNotNull
        assertThat(savedRequest.getTotalTimeOffDay()).isEqualTo(TimeOffDay.HALF)
    }

    @Test
    @DisplayName("반차는 시작일과 종료일이 같아야 한다.")
    fun createHalfDayLeaveExceptionTest() {
        val user = userRepository.findByUsername(USERNAME)

        assertThrows<IllegalArgumentException> {
            HalfDayLeave(
                userId = user?.userId!!,
                period = LeavePeriod(
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now().plusDays(1)
                )
            )
        }

    }

    @Test
    @DisplayName("휴가 신청을 생성한다(연차).")
    fun createFullDayLeaveTest() {
        val user = userRepository.findByUsername(USERNAME)

        val leaveRequest = FullDayLeave(
            userId = user?.userId!!,
            period = LeavePeriod(
                startDate = LocalDate.now(),
                endDate = LocalDate.now()
            )
        )

        val savedRequest = leaveRequestRepository.save(leaveRequest)

        assertThat(savedRequest.getId()).isNotNull
        assertThat(savedRequest.getTotalTimeOffDay()).isEqualTo(TimeOffDay(1))
    }

    @Test
    @DisplayName("휴가 신청을 생성한다(병가).")
    fun createSickLeaceTest() {
        val user = userRepository.findByUsername(USERNAME)

        val leaveRequest = SickLeave(
            userId = user?.userId!!,
            period = LeavePeriod(
                startDate = LocalDate.now(),
                endDate = LocalDate.now()
            )
        )

        val savedRequest = leaveRequestRepository.save(leaveRequest)

        assertThat(savedRequest.getId()).isNotNull
        assertThat(savedRequest.getTotalTimeOffDay()).isEqualTo(TimeOffDay.ZERO)
    }

}