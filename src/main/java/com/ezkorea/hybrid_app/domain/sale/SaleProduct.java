package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.Member;
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
public class SaleProduct extends BaseEntity {

    @Setter
    private String wiperSort;

    @Setter
    private String wiperSize;

    @Setter
    private int wiperPrice;

    @ManyToOne(targetEntity = DailyTask.class, fetch = FetchType.LAZY)
    private DailyTask task;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member seller;

    public void setBasicInfo(Member seller, DailyTask dailyTask) {
        this.task = dailyTask;
        this.seller = seller;
    }
}
