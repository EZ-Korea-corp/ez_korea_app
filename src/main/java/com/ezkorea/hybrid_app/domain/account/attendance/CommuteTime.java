package com.ezkorea.hybrid_app.domain.account.attendance;

import com.ezkorea.hybrid_app.domain.account.member.Member;
import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class CommuteTime extends BaseEntity {

    // 기준 날짜
    private LocalDate date;

    // 출근 상태
    @Setter
    private String status;

    // 출근 시간
    private LocalDateTime onTime;

    // 퇴근 시간
    @Setter
    private LocalDateTime offTime;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;
}
