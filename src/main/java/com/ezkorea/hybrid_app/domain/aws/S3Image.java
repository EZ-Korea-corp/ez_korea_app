package com.ezkorea.hybrid_app.domain.aws;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class S3Image extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String filePath;
    private String fileName;

}
