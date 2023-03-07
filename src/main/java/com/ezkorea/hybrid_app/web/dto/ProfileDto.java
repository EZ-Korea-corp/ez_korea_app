package com.ezkorea.hybrid_app.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileDto {

    private String originPassword;
    private String newPassword;

    private String phone;
    private String name;
    private String email;

}
