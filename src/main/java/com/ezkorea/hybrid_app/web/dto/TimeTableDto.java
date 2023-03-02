package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.timetable.SellProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TimeTableDto {
    private Long stationId;
    private Long memberId;
    private Long id;
    private String part;
    private String memo;

    List<SellProduct> sellDtoList;
    List<SaleProductDto> saleDtoList;
}
