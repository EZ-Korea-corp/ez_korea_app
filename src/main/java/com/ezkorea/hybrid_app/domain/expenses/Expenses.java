package com.ezkorea.hybrid_app.domain.expenses;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Expenses extends BaseEntity {

    private int cost;

    private LocalDate payDate;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "expenses_status")
    @Enumerated(EnumType.STRING)
    private ExpensesStatus expensesStatus;

    @Column(name = "fuel_status")
    @Enumerated(EnumType.STRING)
    @Setter
    private FuelStatus fuelStatus;

    @ManyToOne
    @Setter
    private S3Image s3Image;

    @Setter
    private boolean isManagerCheck;

}
