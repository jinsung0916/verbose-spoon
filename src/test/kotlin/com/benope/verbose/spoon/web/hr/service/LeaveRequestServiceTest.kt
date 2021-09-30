package com.benope.verbose.spoon.web.hr.service

import com.benope.verbose.spoon.BenopeTest
import com.benope.verbose.spoon.core_backend.security.domain.User
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.hr.domain.time_off.PaidTimeOff
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import com.benope.verbose.spoon.web.hr.domain.time_off.ValidityPeriod
import com.benope.verbose.spoon.web.hr.dto.CreateLeaveRequestReq
import com.benope.verbose.spoon.web.hr.dto.DeleteLeaveRequestReq
import com.benope.verbose.spoon.web.hr.repository.TimeOffRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class LeaveRequestServiceTest(
    @Autowired private val leaveRequestService: LeaveRequestService
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

        val timeOffEntity = PaidTimeOff(
            userId = user?.userId!!,
            validityPeriod = ValidityPeriod(LocalDate.now(), LocalDate.now()),
            remainingDays = TimeOffDay(10.0)
        )

        val savedEntity = timeOffRepository.save(timeOffEntity)
    }

    @Test
    @DisplayName("휴가 신청을 생성한다.")
    fun createLeaveRequestTest() {
        val leaveRequestDto = CreateLeaveRequestReq(
            type = LeaveRequestType.FULL_DAY,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            userId = user?.userId,
            approvalLine = listOf(admin?.userId!!)
        )


        val response = leaveRequestService.createLeaveRequest(leaveRequestDto)

        assertThat(response.type).isEqualTo(leaveRequestDto.type)
        assertThat(response.startDate).isEqualTo(leaveRequestDto.startDate)
        assertThat(response.endDate).isEqualTo(leaveRequestDto.endDate)
        assertThat(response.userId).isEqualTo(leaveRequestDto.userId)
        assertThat(response.approvalLine?.size).isEqualTo(leaveRequestDto.approvalLine?.size)
    }

    @Test
    @DisplayName("사용자 PK 로 휴가 신청을 조회한다.")
    fun findByUserIdTest() {
        val leaveRequestDto = CreateLeaveRequestReq(
            type = LeaveRequestType.FULL_DAY,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            userId = user?.userId,
            approvalLine = listOf(admin?.userId!!)
        )


        leaveRequestService.createLeaveRequest(leaveRequestDto)

        val response = leaveRequestService.findByUserId(user?.userId)

        assertThat(response.size).isEqualTo(1)
    }

    @Test
    @DisplayName("승인자 PK로 휴가 신청을 조회한다.")
    fun findByApprovalLineUserIdTest() {
        val leaveRequestDto = CreateLeaveRequestReq(
            type = LeaveRequestType.FULL_DAY,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            userId = user?.userId,
            approvalLine = listOf(admin?.userId!!)
        )


        leaveRequestService.createLeaveRequest(leaveRequestDto)

        val response = leaveRequestService.findByApprovalLineUserId(admin?.userId)

        assertThat(response.size).isEqualTo(1)
    }

    @Test
    @DisplayName("승인된 휴가 신청 목록을 조회한다.")
    fun findAllApprovedTest() {
        val leaveRequestDto = CreateLeaveRequestReq(
            type = LeaveRequestType.FULL_DAY,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            userId = user?.userId,
            approvalLine = listOf(admin?.userId!!)
        )


        val createResponse = leaveRequestService.createLeaveRequest(leaveRequestDto)
        leaveRequestService.approveRequest(createResponse.leaveRequestId, admin?.userId)

        val response = leaveRequestService.findAllApproved(LocalDate.now(), LocalDate.now())

        assertThat(response.size).isEqualTo(1)
    }

    @Test
    @DisplayName("휴가 신청을 삭제한다.")
    fun deleteLeaveRequestTest() {
        val request = CreateLeaveRequestReq(
            type = LeaveRequestType.FULL_DAY,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            userId = user?.userId,
            approvalLine = listOf(admin?.userId!!)
        )

        val createResponse = leaveRequestService.createLeaveRequest(request)

        val deleteRequest = DeleteLeaveRequestReq(
            leaveRequestId = createResponse.leaveRequestId,
            requestUserId =  createResponse.userId
        )

        leaveRequestService.deleteLeaveRequest(deleteRequest)

        val leaveRequest = leaveRequestService.findByUserId(createResponse.userId)
        assertThat(leaveRequest.size).isEqualTo(0)
    }

    @Test
    @DisplayName("휴가 신청을 삭제한다.")
    fun preventDuplicatedDeleteLeaveRequestTest() {
        val request = CreateLeaveRequestReq(
            type = LeaveRequestType.FULL_DAY,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            userId = user?.userId,
            approvalLine = listOf(admin?.userId!!)
        )

        val createResponse1 = leaveRequestService.createLeaveRequest(request)
        val createResponse2 = leaveRequestService.createLeaveRequest(request)

        val deleteRequest = DeleteLeaveRequestReq(
            leaveRequestId = createResponse1.leaveRequestId,
            requestUserId =  createResponse1.userId
        )

        leaveRequestService.deleteLeaveRequest(deleteRequest)

        assertThat(leaveRequestService.findByUserId(createResponse2.userId).size).isEqualTo(1)
    }

}