package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.web.hr.domain.LeaveRequest
import com.benope.verbose.spoon.web.hr.domain.TimeOff
import com.benope.verbose.spoon.web.hr.exception.NotEnoughTimeOffException
import org.springframework.data.domain.AbstractAggregateRoot
import java.util.stream.Collectors
import kotlin.streams.toList

/**
 * 잔여 연차 일수와 우선순위에 따라 각 연차에 처리를 위임하는 객체
 */
class DelegatingTimeOff(
    private val timeOffList: List<TimeOffEntity>,
    private val comparator: Comparator<TimeOffEntity>
) : TimeOff, AbstractAggregateRoot<DelegatingTimeOff>() {

    override fun supports(leaveRequest: LeaveRequest): Boolean {
        // 모든 휴가 신청을 파라미터로 받아 적절한 연차에 위임한다.
        return true
    }

    override fun useTimeOff(leaveRequest: LeaveRequest, requiredTimeOffDay: TimeOffDay): TimeOffDay {

        var remainDays = requiredTimeOffDay

        for (timeOff in sortedTimeOffList()) {
            val processedTimeOffDays = timeOff.useTimeOff(leaveRequest, remainDays)
            remainDays = remainDays.minus(processedTimeOffDays)
            if (remainDays == TimeOffDay.ZERO) {
                break
            }
        }

        return if (remainDays != TimeOffDay.ZERO) {
            throw NotEnoughTimeOffException()
        } else {
            requiredTimeOffDay
        }

    }

    private fun sortedTimeOffList(): List<TimeOff> {
        return this.timeOffList.stream()
            .sorted(comparator)
            .collect(Collectors.toList())
    }

    override fun undoUseTimeOff(canceledApplicationId: Long?) {
        this.timeOffList.stream().forEach { it.undoUseTimeOff(canceledApplicationId) }
    }

    override fun toEntityList(): List<TimeOffEntity> {
        return this.timeOffList.stream()
            .map { it.toEntityList() }
            .flatMap { it.stream() }
            .toList()
    }

    override fun remainingDays(): TimeOffDay {
        return this.timeOffList.stream()
            .map { it.remainingDays() }
            .reduce { o1, o2 -> o1.plus(o2) }
            .orElse(TimeOffDay.ZERO)
    }

    override fun usedDays(): TimeOffDay {
        return this.timeOffList.stream()
            .map { it.usedDays() }
            .reduce { o1, o2 -> o1.plus(o2) }
            .orElse(TimeOffDay.ZERO)
    }

    override fun getType(): TimeOffType {
        return TimeOffType.PAID
    }

}