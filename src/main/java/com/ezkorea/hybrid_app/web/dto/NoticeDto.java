package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<S3Image> imageList;
    private LocalDateTime createDate;
}
