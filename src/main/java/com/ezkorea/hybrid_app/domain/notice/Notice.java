package com.ezkorea.hybrid_app.domain.notice;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.member.Member;
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
    private List<S3Image> imageList = new ArrayList<>();

}
