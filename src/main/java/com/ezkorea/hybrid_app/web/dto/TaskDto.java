package com.ezkorea.hybrid_app.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TaskDto {
    private Long stationId;
    private Long memberId;

    List<WiperDto> wiperDtoList;
    List<SaleProductDto> saleDtoList;
}
