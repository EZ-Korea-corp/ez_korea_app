package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.post.Post;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
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
public class SaleProduct extends BaseEntity {

    @OneToOne
    private Wiper wiper;

    @ManyToOne(targetEntity = DailyTask.class, fetch = FetchType.LAZY)
    private DailyTask task;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member seller;

}
