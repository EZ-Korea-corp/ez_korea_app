package com.ezkorea.hybrid_app.domain.expenses.fuel;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int gasolinePrice;
    private int dieselPrice;
    private LocalDate baseDate;

}
