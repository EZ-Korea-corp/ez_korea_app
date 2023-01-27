package com.ezkorea.hybrid_app.domain.task;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
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
public class DailyTask extends BaseEntity {

    private LocalDate taskDate;
    private boolean isClose;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SaleProduct> productList = new ArrayList<>();

    @OneToOne
    @Setter
    private GasStation gasStation;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    public void addProduct(SaleProduct product) {
        productList.add(product);
    }
}
