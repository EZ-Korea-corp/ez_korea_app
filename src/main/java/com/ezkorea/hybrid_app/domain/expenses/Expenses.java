package com.ezkorea.hybrid_app.domain.expenses;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Expenses extends BaseEntity {

    private int cost;

    @Column(name = "expenses_status")
    @Enumerated(EnumType.STRING)
    private ExpensesStatus expensesStatus;

    @Column(name = "fuel_status")
    @Enumerated(EnumType.STRING)
    private FuelStatus fuelStatus;

    @ManyToOne
    @Setter
    private S3Image s3Image;

}
