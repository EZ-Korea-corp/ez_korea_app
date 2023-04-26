package com.ezkorea.hybrid_app.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class NoticeDto {

    private Long id;
    private String title;
    private String content;
}
