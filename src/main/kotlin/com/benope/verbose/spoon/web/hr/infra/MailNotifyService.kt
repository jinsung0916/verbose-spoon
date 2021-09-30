package com.benope.verbose.spoon.web.hr.infra

import com.benope.verbose.spoon.web.hr.domain.leave_request.LeaveRequestEntity
import com.benope.verbose.spoon.web.hr.service.LeaveRequestNotifyService
import com.benope.verbose.spoon.web.user.exception.UserNotExistsException
import com.benope.verbose.spoon.web.user.service.UserManageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
class MailNotifyService(
    private val userManageService: UserManageService,
    private val emailSender: MailSender
) : LeaveRequestNotifyService {

    @Value("\${spring.mail.username}")
    lateinit var from: String

    override fun notifyCreateToApprovalAuthority(entity: LeaveRequestEntity) {
        val user = userManageService.findUserById(entity.getRequestUserId())

        val to = entity.approvalLine.stream()
            .map {
                try {
                    return@map userManageService.findUserById(it.userId)
                } catch (e: UserNotExistsException) {
                    return@map null
                }
            }
            .filter { it != null }
            .map { it?.email?.value }
            .filter { it != null }
            .toArray { arrayOf(it.toString()) }

        val message = SimpleMailMessage()
        message.setFrom(from)
        message.setTo(*to)
        message.setSubject("[HR] 휴가 승인 요청 알림")
        message.setText("${user.nickname} 의 휴가 요청을 처리해 주세요.")
        emailSender.send(message)

    }

    override fun notifyApprovedToUser(leaveRequestEntity: LeaveRequestEntity) {
        val user = userManageService.findUserById(leaveRequestEntity.getRequestUserId())

        user.email?.let {
            val message = SimpleMailMessage()
            message.setFrom(from)
            message.setTo(it.value)
            message.setSubject("[HR] 휴가 승인 처리 알림")
            message.setText("휴가 승인 처리 되었습니다.")
            emailSender.send(message)
        }
    }
}
