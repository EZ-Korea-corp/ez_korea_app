package com.ezkorea.hybrid_app.domain.post;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    private String title;

    private String content;

    public void setBasicInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }
}