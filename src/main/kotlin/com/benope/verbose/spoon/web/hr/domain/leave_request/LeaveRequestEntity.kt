package com.benope.verbose.spoon.web.hr.domain.leave_request

import com.benope.verbose.spoon.core_backend.common.audit.AuditEntity
import com.benope.verbose.spoon.web.hr.domain.LeaveRequest
import com.benope.verbose.spoon.web.hr.exception.ApprovalLineNotAuthorizedException
import com.benope.verbose.spoon.web.hr.exception.LeaveRequestUnableToDeleteException
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "leave_request")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
abstract class LeaveRequestEntity(
    private var userId: Long,
    @Embedded
    protected var period: LeavePeriod
) : LeaveRequest, AuditEntity<LeaveRequestEntity>() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var leaveRequestId: Long? = null

    @ElementCollection
    @CollectionTable(name = "leave_request_approval_line")
    @AttributeOverrides(
        value = [AttributeOverride(name = "userId", column = Column(name = "approval_authority"))]
    )
    private var approvalLine: MutableList<ApprovalLine> = mutableListOf()

    override fun getId(): Long? {
        return this.leaveRequestId
    }

    override fun getRequestUserId(): Long {
        return this.userId
    }

    fun addApprovalLine(approvalLine: ApprovalLine) {
        this.approvalLine.add(approvalLine)
    }

    fun approveRequest(userId: Long?) {
        val approveLine = findApprovalLine(userId) ?: throw ApprovalLineNotAuthorizedException()
        approveLine.isApproved = true
        approveLine.approveDateTime = LocalDateTime.now()
    }

    private fun findApprovalLine(userId: Long?): ApprovalLine? {
        return this.approvalLine.stream()
            .filter { it.userId == userId }
            .findAny()
            .orElse(null)
    }

    fun isApproved(): Boolean {
        return this.approvalLine.stream()
            .allMatch { it.isApproved }
    }

    override fun markDeleted() {
        if (isApproved()) {
            throw LeaveRequestUnableToDeleteException()
        } else {
            super.markDeleted()
        }
    }

}