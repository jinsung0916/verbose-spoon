package com.benope.verbose.spoon.web.hr.domain.time_off

import com.benope.verbose.spoon.core_backend.common.audit.AuditEntity
import com.benope.verbose.spoon.web.hr.domain.TimeOff
import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestType
import com.benope.verbose.spoon.web.hr.exception.TimeOffUnableToDeleteException
import javax.persistence.*

@Entity
@Table(name = "time_off")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
abstract class TimeOffEntity(

    private var userId: Long,

    @Embedded
    open var validityPeriod: ValidityPeriod,

    @Embedded
    @AttributeOverrides(
        value = [AttributeOverride(name = "days", column = Column(name = "remaining_days"))]
    )
    private var remainingDays: TimeOffDay

) : TimeOff, AuditEntity<TimeOffEntity>() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var timeOffId: Long? = null
        protected set

    @ElementCollection
    @CollectionTable(
        name = "time_off_usage_history",
        joinColumns = [JoinColumn(name = "time_off_id")]
    )
    private var timeOffUsageHistory: MutableSet<TimeOffUsageHistory> = mutableSetOf()

    open var reason: String? = null

    private fun available(): Boolean {
        return validityPeriod.valid() && hasRemainingDays()
    }

    private fun hasRemainingDays(): Boolean {
        return this.remainingDays != TimeOffDay.ZERO
    }

    override fun useTimeOff(
        leaveRequestId: Long?,
        leaveRequestType: LeaveRequestType?,
        requiredTimeOffDay: TimeOffDay?
    ): TimeOffDay {
        requiredTimeOffDay ?: throw IllegalArgumentException("RequiredTimeOffDay cannot be null.")

        if (!available() || !supports(leaveRequestType)) {
            return TimeOffDay.ZERO
        }

        val remainingDays = requiredTimeOffDay.minus(this.remainingDays)

        if (remainingDays == TimeOffDay.ZERO) {
            // 보유 연차로 모든 휴가 신청을 처리한 경우, requiredTimeOffDay 만큼 사용 처리한다.
            pushHistory(leaveRequestId, requiredTimeOffDay)
            this.remainingDays = this.remainingDays.minus(requiredTimeOffDay)
        } else {
            // 보유 연차로 모든 휴가 신청을 처리하지 못한 경우, remainingDays 만큼만 사용 처리한다.
            pushHistory(leaveRequestId, this.remainingDays)
            this.remainingDays = TimeOffDay.ZERO
        }

        return getTimeOffUsageHistory(leaveRequestId).usedDays
    }

    private fun pushHistory(applicationId: Long?, usedDays: TimeOffDay) {
        val history = TimeOffUsageHistory(
            leaveApplicationId = applicationId,
            usedDays = usedDays
        )
        this.timeOffUsageHistory.add(history)
    }

    private fun getTimeOffUsageHistory(applicationId: Long?): TimeOffUsageHistory {
        return this.timeOffUsageHistory.stream()
            .filter { it.getId() == applicationId }
            .findAny()
            .get()
    }

    override fun undoUseTimeOff(canceledApplicationId: Long?) {
        val usedDays = timeOffUsageHistory.stream()
            .filter { it.getId() == canceledApplicationId }
            .findAny()
            .map { it.undoHistory() }
            .orElse(TimeOffDay.ZERO)

        this.remainingDays = this.remainingDays.plus(usedDays)
    }

    override fun toEntityList(): List<TimeOffEntity> {
        return listOf(this)
    }

    override fun remainingDays(): TimeOffDay {
        return this.remainingDays
    }

    override fun usedDays(): TimeOffDay {
        return this.timeOffUsageHistory.stream()
            .filter { !it.isCanceled }
            .map { it.usedDays }
            .reduce { o1, o2 -> o1.plus(o2) }
            .orElse(TimeOffDay.ZERO)
    }

    override fun markDeleted() {
        if (usedDays() == TimeOffDay.ZERO) {
            super.markDeleted()
        } else {
            throw TimeOffUnableToDeleteException()
        }
    }

}