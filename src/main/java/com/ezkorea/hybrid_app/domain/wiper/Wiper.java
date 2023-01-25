package com.ezkorea.hybrid_app.domain.wiper;

import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Wiper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String wiperSort;
    private String wiperSize;
    private int wiperPrice;
    private String wiperViewName;

    @OneToOne
    private SaleProduct saleProduct;
}
