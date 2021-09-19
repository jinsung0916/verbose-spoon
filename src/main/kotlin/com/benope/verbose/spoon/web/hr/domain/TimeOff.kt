package com.benope.verbose.spoon.web.hr.domain

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffDay
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity
import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffType

interface TimeOff {

    /**
     * 주어진 휴가 신청을 처리할 수 있는지 확인한다.
     *
     * @param leaveRequestType 휴가 신청 유형
     * @return 처리 가능 여부
     */
    fun supports(leaveRequestType: LeaveRequestType?): Boolean

    /**
     * 휴가 신청을 처리한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @param leaveRequestType 휴가 신청 유형
     * @param requiredTimeOffDay 휴가 신청 일수
     * @return 처리된 휴가 신청 일수
     */
    fun useTimeOff(leaveRequestId: Long?, leaveRequestType: LeaveRequestType?, requiredTimeOffDay: TimeOffDay?): TimeOffDay

    /**
     * 휴가 신청을 취소한다.
     *
     * @param canceledApplicationId 취소할 휴가 신청 ID
     */
    fun undoUseTimeOff(canceledApplicationId: Long?)

    /**
     *  TimeOffEntity 를 영속화 하기 위한 Getter method.
     *
     *  @return Immutable list of TimeOffEntity
     */
    fun toEntityList(): List<TimeOffEntity>

    /**
     *  잔여 연차를 반환한다.
     *
     *  @return Immutable list of TimeOffEntity
     */
    fun remainingDays(): TimeOffDay

    /**
     *  사용된 연차를 반환한다.
     *
     *  @return Immutable list of TimeOffEntity
     */
    fun usedDays(): TimeOffDay

    /**
     * 연차 유형을 반환한다.
     *
     * @return TimeOffType enum object.
     */
    fun getType() : TimeOffType

}