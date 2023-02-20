package com.ezkorea.hybrid_app.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TaskDto {
    private Long stationId;
    private Long memberId;
    private int rn;

    List<WiperDto> wiperDtoList;
    List<SaleProductDto> saleDtoList;
}
