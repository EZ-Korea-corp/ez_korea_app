package com.ezkorea.hybrid_app.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MealDto {
    private String name;
    private String phone;
    private LocalDateTime checkTime;
}
