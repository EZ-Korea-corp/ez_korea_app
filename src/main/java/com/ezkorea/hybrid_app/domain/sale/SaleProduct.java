package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class SaleProduct extends BaseEntity {

    private int count;

    @OneToOne
    private Wiper wiper;

    @ManyToOne(targetEntity = TimeTable.class, fetch = FetchType.LAZY)
    private TimeTable timeTable;
}
