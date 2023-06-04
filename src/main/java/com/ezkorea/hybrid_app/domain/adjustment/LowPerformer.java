package com.ezkorea.hybrid_app.domain.adjustment;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
/* 통계용 테이블 */
public class LowPerformer extends BaseEntity {
    @ManyToOne(targetEntity = Adjustment.class, fetch = FetchType.LAZY)
    private Adjustment adjustment;
    private long memberId;
    @Enumerated(EnumType.STRING)
    private PartTime partTime;
    private int sales;
}
