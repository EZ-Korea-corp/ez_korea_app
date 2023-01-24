package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class SaleProduct extends BaseEntity {
    private WiperSort wiperSort;
    private LocalDateTime saleTime;
}
