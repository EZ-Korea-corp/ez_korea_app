package com.ezkorea.hybrid_app.domain.timetable;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class TimeTable extends BaseEntity {

    @ManyToOne
    private GasStation gasStation;

    @ManyToOne
    private Member member;

    private LocalDate taskDate;

    //오전, 오후, 풀타임
    private String part;

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private List<SellProduct> sellList = new ArrayList<>();

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private List<SaleProduct> SaleList = new ArrayList<>();
}
