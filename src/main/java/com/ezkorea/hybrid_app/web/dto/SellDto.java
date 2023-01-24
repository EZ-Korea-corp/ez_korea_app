package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.sale.WiperSize;
import com.ezkorea.hybrid_app.domain.sale.WiperSort;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SellDto {
    private String wiperSort;
    private String wiperSize;
    private int wiperPrice;
}
