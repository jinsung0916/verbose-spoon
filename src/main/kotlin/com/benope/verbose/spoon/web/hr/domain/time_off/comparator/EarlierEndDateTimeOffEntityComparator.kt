package com.benope.verbose.spoon.web.hr.domain.time_off.comparator

import com.benope.verbose.spoon.web.hr.domain.time_off.TimeOffEntity
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
/**
 * 연차 소진 우선순위 부여를 위한 Comparator 인터페이스 구현체.
 * 유효 기간이 빠른 연차부터 우선 소진시킨다(기본 전략).
 */
class EarlierEndDateTimeOffEntityComparator : Comparator<TimeOffEntity> {
    override fun compare(o1: TimeOffEntity?, o2: TimeOffEntity?): Int {
        return o1?.validityPeriod?.endDate?.compareTo(o2?.validityPeriod?.endDate) ?: 0
    }
}