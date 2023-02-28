package com.ezkorea.hybrid_app.domain.expenses.fuel;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelCost extends BaseEntity {

    private int gasolinePrice;
    private int dieselPrice;

}
