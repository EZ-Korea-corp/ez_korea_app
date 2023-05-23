package com.ezkorea.hybrid_app.domain.adjustment;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
/* 통계용 테이블 */
public class DayOffMember extends BaseEntity {
    @ManyToOne(targetEntity = Adjustment.class, fetch = FetchType.LAZY)
    private Adjustment adjustment;
    private long memberId;
}
