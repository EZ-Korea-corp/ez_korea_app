package com.ezkorea.hybrid_app.domain.notice;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.read.MemberPostRead;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Notice extends BaseEntity {

    private String title;
    private String content;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member writer;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberPostRead> readList = new ArrayList<>();

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3Image> imageList = new ArrayList<>();

    public void setBasicInfo(NoticeDto dto, Member member) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.imageList.clear();
        this.writer = member;
    }

}
