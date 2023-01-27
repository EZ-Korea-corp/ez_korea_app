package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.user.division.Position;
import com.ezkorea.hybrid_app.domain.user.division.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DivisionDto {
    private String username;
    private Status status;
    private Position position;

}
