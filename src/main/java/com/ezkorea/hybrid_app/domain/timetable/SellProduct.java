package com.ezkorea.hybrid_app.domain.timetable;

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
public class SellProduct extends BaseEntity {

    private String sort;
    private String payment;
    private String status;
    private String memo;

    @Setter
    private int count;

    @ManyToOne(targetEntity = TimeTable.class, fetch = FetchType.LAZY)
    private TimeTable timeTable;
}
