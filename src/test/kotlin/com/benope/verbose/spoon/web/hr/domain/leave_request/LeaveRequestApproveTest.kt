package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.exception.ApprovalLineNotAuthorizedException
import com.benope.verbose.spoon.web.hr.repository.LeaveRequestRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class LeaveRequestApproveTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val leaveRequestRepository: LeaveRequestRepository
) : BenopeTest() {

    @BeforeEach
    fun setUp() {
        val user = userRepository.findByUsername(USERNAME)

        val leaveRequest = FullDayLeave(
            userId = user?.userId!!,
            period = LeavePeriod(
                startDate = LocalDate.now(),
                endDate = LocalDate.now()
            )
        )

        leaveRequestRepository.save(leaveRequest)
    }

    private fun getAnyEntity(): LeaveRequestEntity {
        return leaveRequestRepository.findAll().stream().findAny().get()
    }

    @Test
    @DisplayName("승인권자를 등록한다.")
    fun addApprovalLineTest() {
        val leaveRequest = getAnyEntity()

        val admin = userRepository.findByUsername(ADMIN_USERNAME)
        val approvalLine = ApprovalLine.fromUser(admin)
        leaveRequest.addApprovalLine(approvalLine)

        val savedRequest = leaveRequestRepository.save(leaveRequest)

        Assertions.assertThat(savedRequest.isApproved()).isFalse
    }

    @Test
    @DisplayName("승인권자를 등록한다. - 승인권자의 권한이 불충분할 경우 예외를 발생시킨다.")
    fun addApprovalLineExceptionTest() {
        assertThrows<ApprovalLineNotAuthorizedException> {
            val user = userRepository.findByUsername(USERNAME)
            ApprovalLine.fromUser(user)
        }
    }

    @Test
    @DisplayName("휴가 신청을 승인한다.")
    fun approveLeaveRequestTest() {
        val admin = userRepository.findByUsername(ADMIN_USERNAME)
        val approvalLine = ApprovalLine.fromUser(admin)
        val leaveRequest = getAnyEntity()
        leaveRequest.addApprovalLine(approvalLine)

        leaveRequest.approveRequest(admin?.userId)

        val savedRequest = leaveRequestRepository.save(leaveRequest)

        Assertions.assertThat(savedRequest.isApproved()).isTrue
    }

    @Test
    @DisplayName("휴가 신청을 승인한다. - 승인권자의 권한이 불충분할 경우 예외를 발생시킨다.")
    fun approveLeaveRequestExceptionTest() {
        val admin = userRepository.findByUsername(ADMIN_USERNAME)
        val approvalLine = ApprovalLine.fromUser(admin)
        val leaveRequest = getAnyEntity()
        leaveRequest.addApprovalLine(approvalLine)

        assertThrows<ApprovalLineNotAuthorizedException> {
            val user = userRepository.findByUsername(USERNAME)
            leaveRequest.approveRequest(user?.userId)
        }
    }

}