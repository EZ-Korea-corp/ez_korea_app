package com.ezkorea.hybrid_app.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect
public class FindPasswordDto {
    private String email;
    private String phone;
}
